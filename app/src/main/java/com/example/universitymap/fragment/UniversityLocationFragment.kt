package com.example.universitymap.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations.map
import com.example.universitymap.R
import com.example.universitymap.data.Datasource
import com.example.universitymap.databinding.FragmentUniversityLocationBinding
import com.example.universitymap.model.University
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class UniversityLocationFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentUniversityLocationBinding? = null

    private var mapFragment: SupportMapFragment? = null

    private val binding get() = _binding!!
    private val universities = Datasource().loadUniversities()

    private val callback = OnMapReadyCallback { googleMap ->

        val selectedUniversityName: String = binding.spinnerUniversity.selectedItem as String
        val selectedUniversity: List<University> = universities.filter { university -> university.name == selectedUniversityName }

        (activity as AppCompatActivity).supportActionBar?.title = selectedUniversity[0].name

        val university = selectedUniversity[0].location
        googleMap.addMarker(MarkerOptions().position(university).title(selectedUniversity[0].name))
        googleMap.setMinZoomPreference(17f)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(university))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUniversityLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateSpinnerUniversity()

        val spinnerUniversity = binding.spinnerUniversity
        spinnerUniversity.onItemSelectedListener = this

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
    }

    private fun populateSpinnerUniversity() {
        val universities: MutableList<String> = mutableListOf()
        for (university in Datasource().loadUniversities()) {
            universities.add(university.name)
        }

        val adapter: ArrayAdapter<String>? = this.context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                universities
            )
        }

        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerUniversity.adapter = adapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        mapFragment?.getMapAsync(callback)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}