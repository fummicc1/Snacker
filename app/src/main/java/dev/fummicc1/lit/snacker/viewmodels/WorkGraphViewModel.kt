package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dev.fummicc1.lit.snacker.Constants
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.StockHistoryInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.StockHistoryUseCase
import dev.fummicc1.lit.snacker.repositories.interfaces.IStockHistoryRepository
import dev.fummicc1.lit.snacker.models.StockHistoryPresentable
import dev.fummicc1.lit.snacker.repositories.impl.StockHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*


class WorkGraphViewModel(application: Application) : AndroidViewModel(application) {

    private val stockHistoryUseCase: StockHistoryUseCase = StockHistoryInteractor(application as MyApplication)

    private val _stockHistories: MutableLiveData<List<StockHistoryPresentable>> = MutableLiveData()
    private val _barData: MutableLiveData<BarData> = MutableLiveData()
    private val _fromDate: MutableLiveData<Date> = MutableLiveData()
    private val _endDate: MutableLiveData<Date> = MutableLiveData()
    private val _showingDateList: MutableLiveData<List<Date>> = MutableLiveData()

    val barData: LiveData<BarData> = _barData
    val showingDateList: LiveData<List<Date>> = _showingDateList

    init {
        val calendar = Calendar.getInstance()
        val current = Date()
        calendar.time = current
        calendar.add(Calendar.DATE, 7)

        val end = calendar.time

        _fromDate.postValue(current)
        _endDate.postValue(end)

        viewModelScope.launch(Dispatchers.IO) {
            stockHistoryUseCase.observeAll().collect {
                viewModelScope.launch(Dispatchers.Main) {
                    _stockHistories.postValue(it)

                    val matchedHistories: MutableList<StockHistoryPresentable> = mutableListOf()
                    val showingDateList: MutableList<Date> = mutableListOf()

                    for (i in 0..6) {
                        calendar.time = current
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        val currentDate = calendar.get(Calendar.DATE)
                        calendar.set(Calendar.DATE, currentDate - (6 - i))
                        val date = calendar.time

                        showingDateList.add(date)
                    }

                    for (date in showingDateList) {
                        var matchHistory = it.filter {
                            calendar.time = it.date
                            calendar.get(Calendar.HOUR_OF_DAY) == 0
                        }.filter { history ->
                            val diff = ChronoUnit.DAYS.between(
                                LocalDateTime.ofInstant(
                                    date.toInstant(),
                                    ZoneOffset.systemDefault()
                                ),
                                LocalDateTime.ofInstant(
                                    history.date.toInstant(),
                                    ZoneOffset.systemDefault()
                                )
                            ).toInt()
                            diff == 0
                        }.firstOrNull()

                        if (matchHistory == null) {
                            matchHistory = StockHistoryPresentable(0, date, 0)
                        }
                        matchedHistories.add(matchHistory)
                    }

                    val yData: List<Float> = matchedHistories.map {
                        it.count.toFloat()
                    }

                    val xData = matchedHistories.indices.map {
                        it.toFloat()
                    }

                    val entryList = mutableListOf<BarEntry>()

                    for (i in xData.indices) {
                        val entry = BarEntry(xData[i], yData[i])
                        entryList.add(entry)
                    }

                    val barDataSets = mutableListOf<IBarDataSet>()
                    val dataSet = BarDataSet(entryList, "ストック数")
                    dataSet.formLineWidth = 0f
                    dataSet.color = Constants.primaryColor(application)
                    barDataSets.add(dataSet)

                    val barData = BarData(barDataSets)
                    _barData.postValue(barData)
                    _showingDateList.postValue(showingDateList)
                }
            }
        }
    }

    class DateValueFormatter(private val dateList: List<Date>) :
        ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MM/dd")
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            val axisValue = value.toInt()
            return if (axisValue >= 0 && axisValue < dateList.size) {
                dateFormat.format(dateList[axisValue])
            } else {
                "aaa"
            }
        }
    }
}