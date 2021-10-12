package com.example.zebrascannerappdemo.presentation.ui.main.fragments.register

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.databinding.FragmentRegisterBinding
import com.example.zebrascannerappdemo.domain.exception.ValidationError
import com.example.zebrascannerappdemo.domain.model.JobInfo
import com.example.zebrascannerappdemo.domain.model.Product
import com.example.zebrascannerappdemo.presentation.ui.base.BaseFragment
import com.example.zebrascannerappdemo.presentation.ui.base.HorizontalDividerItemDecoration
import com.example.zebrascannerappdemo.presentation.ui.base.viewBinding
import com.example.zebrascannerappdemo.presentation.ui.main.fragments.register.adapter.ProductAdapter
import com.example.zebrascannerappdemo.presentation.ui.main.fragments.register.adapter.ProductItemClickListener
import com.example.zebrascannerappdemo.presentation.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterStockFragment : BaseFragment(R.layout.fragment_register) {

    private val viewModel: RegisterStockViewModel by viewModels()
    private val binding: FragmentRegisterBinding by viewBinding()

    private var _productsAdapter: ProductAdapter? = null
    private val productsAdapter: ProductAdapter get() = _productsAdapter!!

    private var _reasonList: List<String>? = null
    private val reasonList get() = _reasonList!!

    private var isFormNotEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildReasonList()
        setupViews()

        launchAndRepeatWithViewLifecycle {
            viewModel.registerUIState
                .collect(::handleUIState)
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.registerEvent
                .collect(::handleEvent)
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.stockJob
                .collect(::fillJobData)
        }
    }

    private fun buildReasonList() {
        _reasonList = resources.getStringArray(R.array.register_reasons).asList()
    }

    private fun setupViews() {
        setupRecycler()
        setupReasonSuggestions()

        with(binding) {
            containerEt.setOnEditorActionListener(onEditorAction)
            containerEt.addTextChangedListener {
                clearErrors()
                viewModel.containerBarcodeParams = it.toString()
            }

            itemEt.setOnEditorActionListener(onEditorAction)
            itemEt.addTextChangedListener {
                clearErrors()
                viewModel.itemBarcodeParams = it.toString()
            }

            resetBtn.setOnClickListener {
                onResetClicked(getString(R.string.msg_are_you_sure_you_want_to_clear_all)) {
                    viewModel.clearJob()
                }
            }

            registerBtn.setOnClickListener {
                viewModel.onRegisterClicked()
            }
        }
    }

    private fun setupRecycler() {
        _productsAdapter = ProductAdapter(listener)
        with(binding.itemRecycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productsAdapter
            addItemDecoration(
                HorizontalDividerItemDecoration(requireContext())
            )
        }
    }

    override fun onBackPressed(): Boolean {
        if (isFormNotEmpty) {
            onResetClicked(getString(R.string.msg_clear_form)) {
                viewModel.onBackButtonClicked()
                findNavController().popBackStack()
            }
            return true
        }
        return false
    }

    private fun onResetClicked(message: String, confirm: () -> Unit) {
        clearErrors()
        showConfirmDialog(
            message,
            confirm = {
                confirm()
            }
        )
    }

    private fun setupReasonSuggestions() {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_suggestion_list, reasonList)
        binding.reasonTv.setAdapter(adapter)

        binding.reasonTv.setOnItemClickListener { _, _, position, _ ->
            clearErrors()
            val reason = reasonList[position]
            viewModel.registerReasonParams = reason
        }
    }

    private fun clearErrors() {
        with(binding) {
            reasonTil.error = null
            containerTil.error = null
            scannedItemsErrorTv.isVisible = false
        }
    }

    private fun handleUIState(state: RegisterStockViewModel.RegisterUIStates?) {
        when (state) {
            is RegisterStockViewModel.RegisterUIStates.ShowLoader -> {
                showLoader(state.show)
            }
            is RegisterStockViewModel.RegisterUIStates.ShowEmptyFieldsError -> {
                showEmptyFieldsError(state.errors)
            }
        }
    }

    private fun showEmptyFieldsError(errors: List<ValidationError>) {
        errors.forEach { error ->
            when (error.fieldId) {
                FIELD_REASON -> {
                    binding.reasonTil.error = getString(R.string.error_empty_field)
                }
                FIELD_CONTAINER -> {
                    binding.containerTil.error = getString(R.string.error_empty_field)
                }
                FIELD_PRODUCTS -> {
                    binding.scannedItemsErrorTv.isVisible = true
                }
            }
        }
    }

    private fun handleEvent(event: RegisterStockViewModel.RegisterEvent) {
        when (event) {
            is RegisterStockViewModel.RegisterEvent.JobRegistered -> {
                clearForm()
                showMessage(event.message)
            }
            is RegisterStockViewModel.RegisterEvent.NavigateToDetailsScreen -> {
                showDetailDialog(event.product)
            }
            RegisterStockViewModel.RegisterEvent.ProductAdded -> {
                binding.itemEt.text = null
            }
            RegisterStockViewModel.RegisterEvent.JobRemoved -> toastSuccess()
            is RegisterStockViewModel.RegisterEvent.ShowError -> {
                val message = resources.getErrorMessage(event.exception)
                showErrorMessage(message)
            }
            RegisterStockViewModel.RegisterEvent.ShowValidContainerMessage -> toastSuccess()
        }
    }

    private fun showDetailDialog(product: Product) {
        showMessage(product.barcode, product.productName)
    }

    private fun fillJobData(jobInfo: JobInfo?) {
        with(binding) {
            resetBtn.isVisible = jobInfo != null
            reasonTv.setText(jobInfo?.reason, false)
            containerEt.setText(jobInfo?.container)
            placeholderTv.isVisible = jobInfo?.products.isNullOrEmpty()

            focusField(jobInfo)
        }

        productsAdapter.submitList(jobInfo?.products ?: emptyList())
    }

    private fun focusField(jobInfo: JobInfo?) {
        with(binding) {
            when {
                jobInfo == null || jobInfo.container.isNullOrEmpty() -> {
                    containerEt.requestFocus()
                }
                else -> {
                    itemEt.requestFocus()
                }
            }
        }
    }

    private fun clearForm() {
        with(binding) {
            containerEt.text = null
            reasonTv.text = null
        }
    }

    private fun showLoader(show: Boolean) {
        with(binding) {
            loader.isVisible = show
            formContainer.isVisible = !show
        }
    }

    private val onEditorAction = TextView.OnEditorActionListener { v, actionId, _ ->
        clearErrors()
        return@OnEditorActionListener when (actionId) {
            EditorInfo.IME_ACTION_SEND -> {
                when (v.id) {
                    R.id.container_et -> {
                        viewModel.checkContainer()
                        true
                    }
                    R.id.item_et -> {
                        viewModel.addProduct()
                        true
                    }
                    else -> false
                }
            }
            else -> false
        }
    }

    private val listener = object : ProductItemClickListener {
        override fun onRemoveClicked(product: Product) {
            showConfirmDialog(
                getString(R.string.msg_are_you_sure_you_want_to_decrease_item)
            ) {
                viewModel.onRemoveOneClicked(product)
            }
        }

        override fun onRemoveAllClicked(product: Product) {
            showConfirmDialog(
                getString(R.string.msg_are_you_sure_you_want_to_remove_all_item)
            ) {
                viewModel.onRemoveAllClicked(product)
            }
        }

        override fun onShowInfoClicked(product: Product) {
            viewModel.onShowInfoClicked(product)
        }

    }

    companion object {
        const val FIELD_REASON = 1
        const val FIELD_CONTAINER = 2
        const val FIELD_PRODUCTS = 3
    }
}