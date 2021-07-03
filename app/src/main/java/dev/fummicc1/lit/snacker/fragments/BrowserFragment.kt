package dev.fummicc1.lit.snacker.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dev.fummicc1.lit.snacker.Constants
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.AddSnackActivity
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentBrowserBinding
import dev.fummicc1.lit.snacker.models.AssociatedSnack
import dev.fummicc1.lit.snacker.services.MyWebChromeClient
import dev.fummicc1.lit.snacker.services.MyWebViewClient
import dev.fummicc1.lit.snacker.interfaces.IOnBackPressed
import dev.fummicc1.lit.snacker.interfaces.IWebLoadingProgressIncidactor
import dev.fummicc1.lit.snacker.utils.debounce
import dev.fummicc1.lit.snacker.viewmodels.BrowserViewModel
import dev.fummicc1.lit.snacker.views.AssociatedSnackListAdapter
import dev.fummicc1.lit.snacker.views.ObservableScrollingWebView
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_read_snack.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowserFragment : Fragment(), IOnBackPressed {

    protected lateinit var binding: FragmentBrowserBinding
    protected val viewModel: BrowserViewModel by viewModels()
    protected var webLoadingProgressIncidactor: IWebLoadingProgressIncidactor? = null

    private var initialBrowserIncludeContainerHeight: Int = 0

    var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentBrowserBinding.inflate(layoutInflater, container, false)

        binding.apply {
            val webViewClient = MyWebViewClient()
            val webChromeClient = MyWebChromeClient()
            browserWebView.settings.apply {
                javaScriptEnabled = true
            }
            browserWebView.webViewClient = webViewClient
            browserWebView.webChromeClient = webChromeClient
            browserWebView.loadUrl(Constants.initialWebUrl)

            webChromeClient.handler = object : MyWebChromeClient.Handler {
                override fun onProgressChanged(progress: Int) {
                    webLoadingProgressIncidactor?.onProgress(progress)

                    // 100 is MAXIMUM.
                    if (progress == 100) {
                        webLoadingProgressIncidactor?.onComplete()
                    }
                }
            }

            browserWebView.listener = object : ObservableScrollingWebView.EventListener {
                override val onScrollContentIsAlmostRead: MutableLiveData<Boolean> = MutableLiveData()
                override val onScrollingToDirectionChanged: MutableLiveData<Boolean> = MutableLiveData()
                override val onProgressChanged: MutableLiveData<Int> = MutableLiveData()
            }

            browserWebView.listener?.apply {
                Transformations.distinctUntilChanged(onScrollContentIsAlmostRead).debounce(1000).observe(viewLifecycleOwner, Observer {
                    val shouldShow = it
                    toggleAssociatedListVisibility(shouldShow)

                    if (shouldShow) {
                        viewModel.associatedSnacks.value?.let {
                            if (it.isEmpty()) return@let
                            if (childFragmentManager.fragments.size > 0) return@let
                            val dialogFragment = AssociatedSnackListDialogFragment(it)
                            dialogFragment.show(childFragmentManager, "dialog")
                        }
                    }
                })

                Transformations.distinctUntilChanged(onScrollingToDirectionChanged).debounce(100).observe(viewLifecycleOwner, Observer {
                    toggleAssociatedListVisibility(it)
                })

                Transformations.distinctUntilChanged(onProgressChanged).observe(viewLifecycleOwner, Observer {
                    webLoadingProgressIncidactor?.onProgress(it)
                })
            }

            val recyclerViewAdapter = AssociatedSnackListAdapter(requireContext())
            recyclerViewAdapter.configureEventListner(object :
                AssociatedSnackListAdapter.EventListener {
                override fun onSelectSnack(snack: AssociatedSnack) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val snackPresentable = viewModel.convertAssociatedToSnack(snack)
                        withContext(Dispatchers.Main) {
                            val intent = Intent(requireActivity(), ReadSnackActivity::class.java)
                            intent.putExtra(snackField, snackPresentable)
                            requireActivity().startActivity(intent)
                        }
                    }
                }
            })

            initialBrowserIncludeContainerHeight = browserIncludeContainer.root.height

            val associatedSnackRecyclerView = browserIncludeContainer.associatedSnackRecyclerView
            associatedSnackRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            associatedSnackRecyclerView.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(recyclerViewAdapter)).apply {
                setFirstOnly(false)
            }

            webViewClient.listener = object : MyWebViewClient.Listener {
                override fun onUpdatePage(url: String, title: String?) {
                    viewModel.updateWebPage(url, title)

                    val activity = activity as? AppCompatActivity
                    activity?.supportActionBar?.show()
                    activity?.invalidateOptionsMenu()
                }

                override fun onStartLoadingPage(url: String) {
                    val activity = activity as? AppCompatActivity
                    activity?.supportActionBar?.hide()
                }
            }

            viewModel.associatedSnacks.observe(viewLifecycleOwner, Observer {
                recyclerViewAdapter.injectSnackList(it)
            })
            viewModel.currentWebUrl.observe(viewLifecycleOwner, Observer {
                if (browserWebView.url == it) return@Observer
                browserWebView.loadUrl(it)
            })

            // setup IWebLoadingProgressIncidactor
            configureIWebLoadingProgressIncidactor(requireActivity() as IWebLoadingProgressIncidactor)
        }

        return binding.root
    }

    private fun toggleAssociatedListVisibility(shouldShow: Boolean) {
        binding.browserIncludeContainer.root.apply {
            visibility = if (shouldShow) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        binding.apply {
            if (browserWebView.canGoBack()) {
                val activity = requireActivity() as AppCompatActivity
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_snack_from_browse_icon_button -> {
                val intent = Intent(requireActivity(), AddSnackActivity::class.java)
                viewModel.addSnackPreset?.let {
                    intent.putExtra(AddSnackActivity.addSnackPresetField, it)
                }
                requireActivity().startActivity(intent)
                return true
            }
            R.id.open_in_chrome_icon_button -> {
                val url = binding.browserWebView.url
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                requireActivity().startActivity(intent)
            }
            R.id.back_to_initial_browser -> {
                binding.browserWebView.loadUrl(Constants.initialWebUrl)
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "見つける"
    }

    override fun onBackPressed() {
        binding.apply {
            if (browserWebView.canGoBack()) {
                browserWebView.goBack()
            } else {
                parentFragmentManager.popBackStack()
            }
        }
    }

    fun configureIWebLoadingProgressIncidactor(indicator: IWebLoadingProgressIncidactor) {
        webLoadingProgressIncidactor = indicator
    }

    companion object {
        val snackField: String = "snack_field"
    }

    interface Handler {
        fun updateToolbarVisibility(shouldShow: Boolean)
    }
}