package dev.fummicc1.lit.snacker.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentReadSnackBinding
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.models.AssociatedSnack
import dev.fummicc1.lit.snacker.services.MyWebChromeClient
import dev.fummicc1.lit.snacker.services.MyWebViewClient
import dev.fummicc1.lit.snacker.interfaces.IOnBackPressed
import dev.fummicc1.lit.snacker.interfaces.IWebLoadingProgressIncidactor
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.utils.debounce
import dev.fummicc1.lit.snacker.viewmodels.ReadSnackViewModel
import dev.fummicc1.lit.snacker.views.AssociatedSnackListAdapter
import dev.fummicc1.lit.snacker.views.ObservableScrollingWebView
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReadSnackFragment : Fragment(), IOnBackPressed {

    protected lateinit var binding: FragmentReadSnackBinding
    protected lateinit var viewModel: ReadSnackViewModel
    protected var webLoadingProgressIncidactor: IWebLoadingProgressIncidactor? = null

    var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentReadSnackBinding.inflate(layoutInflater, container, false)

        val snack = requireActivity().intent.getParcelableExtra<SnackPresentable>(BrowserFragment.snackField)
        viewModel = ReadSnackViewModel(requireActivity().application, snack)

        binding.apply {

            changeBookmarkStatusFloatingActionButton.drawable.mutate()
                .setTint(ContextCompat.getColor(requireContext(), R.color.brown))

            val webViewClient = MyWebViewClient()
            val webChromeClient = MyWebChromeClient()
            readSnackWebView.settings.apply {
                javaScriptEnabled = true
            }
            readSnackWebView.webViewClient = webViewClient
            readSnackWebView.webChromeClient = webChromeClient

            webChromeClient.handler = object : MyWebChromeClient.Handler {
                override fun onProgressChanged(progress: Int) {
                    webLoadingProgressIncidactor?.onProgress(progress)

                    // 100 is MAXIMUM.
                    if (progress == 100) {
                        webLoadingProgressIncidactor?.onComplete()
                    }
                }
            }

            val recyclerViewAdapter = AssociatedSnackListAdapter(requireContext())
            recyclerViewAdapter.configureEventListner(object :
                AssociatedSnackListAdapter.EventListener {
                override fun onSelectSnack(snack: AssociatedSnack) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val snackPresentable = viewModel.convertAssociatedToSnack(snack)
                        withContext(Dispatchers.Main) {
                            val intent = Intent(requireActivity(), ReadSnackActivity::class.java)
                            intent.putExtra(BrowserFragment.snackField, snackPresentable)
                            requireActivity().startActivity(intent)
                        }
                    }
                }
            })

            val associatedSnackRecyclerView = readSnackFragmentContainer.associatedSnackRecyclerView
            associatedSnackRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            associatedSnackRecyclerView.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(recyclerViewAdapter)).apply {
                setFirstOnly(false)
            }

            webViewClient.listener = object : MyWebViewClient.Listener {
                override fun onUpdatePage(url: String, title: String?) {
                    viewModel.updateWebPage(url, title)
                    val activity = getActivity() as? AppCompatActivity
                    if (readSnackWebView.canGoBack()) {
                        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    }
                    activity?.supportActionBar?.show()
                }

                override fun onStartLoadingPage(url: String) {
                    val activity = activity as? AppCompatActivity
                    activity?.supportActionBar?.hide()
                }
            }

            readSnackWebView.listener = object : ObservableScrollingWebView.EventListener {
                override val onScrollContentIsAlmostRead: MutableLiveData<Boolean> =
                    MutableLiveData()
                override val onScrollingToDirectionChanged: MutableLiveData<Boolean> =
                    MutableLiveData()
                override val onProgressChanged: MutableLiveData<Int> = MutableLiveData()
            }

            readSnackWebView.listener?.apply {

                Transformations.distinctUntilChanged(onScrollContentIsAlmostRead).debounce(1000)
                    .observe(viewLifecycleOwner, Observer {
                        val shouldShow = it
                        toggleAssociatedListVisibility(shouldShow)
                        toggleFloatingActionButtonVisibility(shouldShow)

                        if (shouldShow) {
                            viewModel.associatedSnacks.value?.let {
                                if (it.isEmpty()) return@let
                                if (childFragmentManager.fragments.size > 0) return@let
                                val dialogFragment = AssociatedSnackListDialogFragment(it)
                                dialogFragment.show(childFragmentManager, "dialog")
                            }
                        }
                    })

                Transformations.distinctUntilChanged(onScrollingToDirectionChanged).debounce(100)
                    .observe(viewLifecycleOwner, Observer {
                        toggleAssociatedListVisibility(it)
                        toggleFloatingActionButtonVisibility(it)
                    })

                Transformations.distinctUntilChanged(onProgressChanged)
                    .observe(viewLifecycleOwner, Observer {
                        webLoadingProgressIncidactor?.onProgress(it)
                    })
            }

            viewModel.associatedSnacks.observe(viewLifecycleOwner, Observer {
                recyclerViewAdapter.injectSnackList(it)
            })
            viewModel.currentWebUrl.observe(viewLifecycleOwner, Observer {
                if (readSnackWebView.url == it) return@Observer
                readSnackWebView.loadUrl(it)
            })

            viewModel.onReceiveMessage.observe(viewLifecycleOwner, Observer {
                Snackbar.make(requireContext(), container!!, it, Snackbar.LENGTH_LONG).show()
            })

            // setup IWebLoadingProgressIncidactor
            configureIWebLoadingProgressIncidactor(requireActivity() as IWebLoadingProgressIncidactor)

            changeBookmarkStatusFloatingActionButton.setOnClickListener {
                viewModel.performArchiveAction()
            }

            viewModel.bookmarkStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    Snack.BookmarkStatus.ARCHIVED -> {
                        changeBookmarkStatusFloatingActionButton.setImageResource(R.drawable.ic_baseline_unarchive_24)
                    }
                    else -> {
                        changeBookmarkStatusFloatingActionButton.setImageResource(R.drawable.ic_baseline_archive_24)
                    }
                }
            })
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.read_snack_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        (requireActivity() as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.open_in_chrome_icon_button -> {
                val url = binding.readSnackWebView.url
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                requireActivity().startActivity(intent)
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "読む"
    }

    override fun onBackPressed() {
        binding.apply {
            if (readSnackWebView.canGoBack()) {
                readSnackWebView.goBack()
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun toggleAssociatedListVisibility(shouldShow: Boolean) {
        binding.readSnackFragmentContainer.root.apply {
            visibility = if (shouldShow) View.VISIBLE else View.GONE
        }
    }

    private fun toggleFloatingActionButtonVisibility(shouldShow: Boolean) {
        binding.changeBookmarkStatusFloatingActionButton.apply {
            visibility = if (shouldShow) View.VISIBLE else View.GONE
        }
    }

    fun configureIWebLoadingProgressIncidactor(indicator: IWebLoadingProgressIncidactor) {
        webLoadingProgressIncidactor = indicator
    }

    interface Handler {
        fun updateToolbarVisibility(shouldShow: Boolean)
    }

    companion object {
        val snackHistoryIdField: String = "snack_history_id_field"
    }
}