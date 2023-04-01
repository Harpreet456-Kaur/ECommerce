package com.example.e_commerce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentPrintedSubCategoryBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PrintedSubCategory.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrintedSubCategory : Fragment(),PrintedInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentPrintedSubCategoryBinding
    var PrintedList= ArrayList<PrintedModel>()
    var printedAdapter: PrintedAdapter?=null
    lateinit var printedInterface: PrintedInterface
    var printedModel= PrintedModel()
    val db= Firebase.firestore
    var isUpdate= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db.collection("Printed").addSnapshotListener { value, error ->
            if (value != null)
                for (snapshots in value!!.documentChanges) {
                    when (snapshots.type) {
                        DocumentChange.Type.ADDED -> {
                            var printedModel = PrintedModel()
                            printedModel =
                                snapshots.document.toObject(printedModel::class.java)
                            printedModel.key = snapshots.document.id ?: ""
                            PrintedList.add(printedModel)
                            printedAdapter?.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED -> {
                            var printedModel = PrintedModel()
                            printedModel = snapshots.document.toObject(printedModel::class.java)
                            printedModel.key = snapshots.document.id ?: ""
                            for (i in 0..PrintedList.size - 1) {
                                if ((snapshots.document.id ?: "").equals(PrintedList[i].key)) {
                                    PrintedList.removeAt(i)
                                    break
                                }
                            }
                            printedAdapter?.notifyDataSetChanged()
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
        binding = FragmentPrintedSubCategoryBinding.inflate(layoutInflater)
        binding.floatingBtn.setOnClickListener {
            findNavController().navigate(R.id.subCategoryAdd)
        }
        binding.btnAdd.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                binding.etName.error = "Enter SubCategory"
            } else {
                if(isUpdate == false) {
                    val printedModel = PrintedModel(name = binding.etName.text.toString())
                    db.collection("Printed")
                        .add(printedModel)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "ADD", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.e("TAG", "Failure ${it}")
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                else{
                    printedModel.name = binding.etName.text.toString()
                    db.collection("Printed").document(printedModel.key?:"")
                        .set(printedModel)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "Data updated", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.e("TAG", "Failure ${it}")
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
               // findNavController().navigate(R.id.subCategories)
            }
        }
        binding?.btnDelete?.setOnClickListener {
            db.collection("Printed")
                .document(printedModel.key ?: "").delete()
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
         * @return A new instance of fragment PrintedSubCategory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrintedSubCategory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun edit(position: Int) {
         isUpdate=true
    }
}