package com.example.zebrascannerappdemo.presentation.ui.base

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> Fragment.viewBinding() =
    FragmentViewBindingDelegate(T::class.java)

class FragmentViewBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
) : ReadOnlyProperty<Fragment, T> {

    internal var viewBinding: T? = null
    private val bindMethod = bindingClass.getMethod("bind", View::class.java)
    private val lifecycleObserver = BindingLifecycleObserver()

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        checkIsMainThread()
        this.viewBinding?.let { return it }

        val view = thisRef.requireView()
        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        val invoke = bindMethod(null, view) as T
        return invoke.also { vb -> this.viewBinding = vb }
    }

    private fun checkIsMainThread() {
        check(Looper.myLooper() == Looper.getMainLooper())
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {

        private val mainHandler = Handler(Looper.getMainLooper())

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            mainHandler.post {
                viewBinding = null
            }
        }
    }
}