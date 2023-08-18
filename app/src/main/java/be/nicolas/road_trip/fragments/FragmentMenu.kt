package be.nicolas.road_trip.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ActionMenuView.OnMenuItemClickListener
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.nicolas.road_trip.R
import be.nicolas.road_trip.databinding.FragmentHomeBinding
import be.nicolas.road_trip.databinding.FragmentMenuBinding
import java.text.ParsePosition

class FragmentMenu : Fragment(R.layout.fragment_menu) {

    private lateinit var binding : FragmentMenuBinding

    enum class SelectedMenu  {
        HOME,
        POI,
        FAV
    }

    interface OnMenuclickListener{
        fun onMenuClick(selectedMenu: SelectedMenu)
    }

    private var onMenuclickListener: OnMenuclickListener? = null
    fun setOnMenuClickListener(onMenuClickListener: OnMenuclickListener){
        this.onMenuclickListener = onMenuClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.home.setOnClickListener { onClickHome() }
        binding.poi.setOnClickListener{ onClickPoi() }
        binding.favori.setOnClickListener{ onClickFav() }

        couleurbtn(SelectedMenu.HOME)

        return binding.root
    }

    private fun onClickHome(){
        onMenuclickListener?.onMenuClick(SelectedMenu.HOME)
        couleurbtn(SelectedMenu.HOME)
    }
    private fun onClickPoi(){
        onMenuclickListener?.onMenuClick(SelectedMenu.POI)
        couleurbtn(SelectedMenu.POI)
    }
    private fun onClickFav(){
        onMenuclickListener?.onMenuClick(SelectedMenu.FAV)
        couleurbtn(SelectedMenu.FAV)
    }


    private fun couleurbtn(selectedMenu: SelectedMenu){
        binding.home.setBackgroundColor(resources.getColor(R.color.white, null))
        binding.poi.setBackgroundColor(resources.getColor(R.color.white, null))
        binding.favori.setBackgroundColor(resources.getColor(R.color.white, null))

        when(selectedMenu){
            SelectedMenu.HOME -> binding.home.setBackgroundColor(resources.getColor(R.color.Mauve, null))
            SelectedMenu.POI -> binding.poi.setBackgroundColor(resources.getColor(R.color.Mauve, null))
            SelectedMenu.FAV -> binding.favori.setBackgroundColor(resources.getColor(R.color.Mauve, null))

        }
    }
}

