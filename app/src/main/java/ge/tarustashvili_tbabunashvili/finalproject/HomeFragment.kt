package ge.tarustashvili_tbabunashvili.finalproject

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Converstation
import ge.tarustashvili_tbabunashvili.finalproject.databinding.HomePageFragmentBinding

class HomeFragment: Fragment() {

    private var _binding: HomePageFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rvConv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("home", "oncreate")
        _binding = HomePageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvConv = view.findViewById(R.id.converstations)
        rvConv.adapter = ConverstationAdapter(emptyList())
    }

    private fun setAdapter(tbs: List<Converstation>) {
        val adapter = ConverstationAdapter(tbs)
        rvConv.adapter = adapter
    }


    private fun getConverstationInfo(): List<Converstation> {
//        Converstation(Drawable.createFromResourceStream(resources, ))
        TODO()
    }

    fun setConverstationsInfo(converstations: List<Converstation>) {
        this.setAdapter(converstations)
    }
}