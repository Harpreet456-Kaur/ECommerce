package com.example.e_commerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.databinding.FragmentSubCategoriesBinding
import com.example.e_commerce.databinding.LayoutItemsBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Sub_Categories.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubCategories : Fragment(), SubCategoryInterface{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSubCategoriesBinding
    var subCategoryList=ArrayList<SubCategoryModel>()
    lateinit var subcategoryAdapter: SubCategoryAdapter
    lateinit var subcategoryInterface: SubCategoryInterface
    var subcategoryModel = SubCategoryModel()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db.collection("SubCategory").addSnapshotListener { value, error ->
            if (value != null)
                for (snapshots in value!!.documentChanges) {
                    when (snapshots.type) {
                        DocumentChange.Type.ADDED -> {
                            var subcategoryModel = SubCategoryModel()
                            subcategoryModel =
                                snapshots.document.toObject(subcategoryModel::class.java)
                            subcategoryModel.key = snapshots.document.id ?: ""
                            subCategoryList.add(subcategoryModel)
                            subcategoryAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED -> {
                            var subcategoryModel = SubCategoryModel()
                            subcategoryModel =
                                snapshots.document.toObject(subcategoryModel::class.java)
                            subcategoryModel.key = snapshots.document.id ?: ""
                            for (i in 0..subCategoryList.size - 1) {
                                if ((snapshots.document.id ?: "").equals(subCategoryList[i].key)) {
                                    subCategoryList.removeAt(i)
                                    break
                                }
                                //categoryAdapter.notifyDataSetChanged()
                            }
                            subcategoryAdapter.notifyDataSetChanged()
                        }
                        else -> {}
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubCategoriesBinding.inflate(layoutInflater)
        subcategoryAdapter = SubCategoryAdapter(subCategoryList, this)
        binding.rvList.adapter = subcategoryAdapter
        binding.rvList.layoutManager = LinearLayoutManager(context)


        binding.floatingBtn.setOnClickListener {
            findNavController().navigate(R.id.subCategoryAdd)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Sub_Categories.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubCategories().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun edit(position: Int) {
        findNavController().navigate(R.id.subCategoryAdd, bundleOf("SubCategory" to subCategoryList[position], "isUpdate" to true))
    }
}