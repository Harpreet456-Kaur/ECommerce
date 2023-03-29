package com.example.e_commerce

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.databinding.FragmentAddItemsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.DocumentChange
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddItems.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddItems : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var  binding: FragmentAddItemsBinding?=null
    var storageRef = FirebaseStorage.getInstance()
    var categoryList = ArrayList<CategoryModel>()
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var newInterface: NewInterface
    var categoryModel = CategoryModel()
    val db = Firebase.firestore
    var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            categoryModel = it.getSerializable("Category") as CategoryModel
            isUpdate = it.getBoolean("isUpdate", false)
        }
        var imagePermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            }
        var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            System.out.println("in PickImage $it")
            binding?.iv?.setImageURI(it)
            it?.let { it1 ->
                storageRef.getReference(Calendar.getInstance().timeInMillis.toString())
                    .putFile(it1)
                    .addOnSuccessListener { uploadTask -> }
            }
        }
    }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): LinearLayout? {
            // Inflate the layout for this fragment
            binding = FragmentAddItemsBinding.inflate(layoutInflater)
            binding?.btnDelete?.setOnClickListener {
                db.collection("Category").document().delete()
                findNavController().navigate(R.id.categories)

            }
            binding?.btnAdd?.setOnClickListener {
                if (binding?.etName?.text?.isEmpty() == true) {
                    binding?.etName?.error = "Enter Category"
                } else {
                    System.out.println(" isUpdate $isUpdate")
                    if(isUpdate == false) {
                        val categoryModel = CategoryModel(name = binding?.etName?.text.toString())
                        db.collection("Category")
                            .add(categoryModel)
                            .addOnSuccessListener {
                                Toast.makeText(requireActivity(), "ADD", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Log.e("TAG", "Failure ${it}")
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                    else{
                        categoryModel.name = binding?.etName?.text.toString()
                        db.collection("Category").document(categoryModel.key?:"")
                            .set(categoryModel)
                            .addOnSuccessListener {
                                Toast.makeText(requireActivity(), "Data updated", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Log.e("TAG", "Failure ${it}")
                                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                    findNavController().navigate(R.id.categories)
                }
            }
            binding?.btnDelete?.setOnClickListener {
                    db.collection("Category")
                        .document(categoryModel.key?:"").delete()
                }
            return binding?.root
        }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddItems.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddItems().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
