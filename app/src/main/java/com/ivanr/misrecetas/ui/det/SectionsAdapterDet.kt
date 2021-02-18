package com.ivanr.misrecetas.ui.det

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ivanr.misrecetas.R
import com.ivanr.misrecetas.fragments.FragmentDetImagenes
import com.ivanr.misrecetas.fragments.FragmentDetNotas
import com.ivanr.misrecetas.fragments.FragmentDetReceta

private val TAB_TITLES = arrayOf(R.string.titulo_det_receta, R.string.titulo_det_notas, R.string.titulo_det_imagenes)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsAdapterDet(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        var v_Fragment: Fragment
        if (position == 0) {
            v_Fragment = FragmentDetReceta.newInstance()
        } else if (position == 1) {
            v_Fragment = FragmentDetNotas.newInstance()
        } else if (position == 2) {
            v_Fragment = FragmentDetImagenes.newInstance()
        } else {
            v_Fragment = PlaceholderFragmentDet.newInstance(position + 1)
        }

        return v_Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}