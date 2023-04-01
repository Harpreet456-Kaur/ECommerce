package com.example.e_commerce

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentSubCategoryAddBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubCategoryAdd.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubCategoryAdd : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSubCategoryAddBinding
    var storageRef = FirebaseStorage.getInstance()
    var subcategoryList = ArrayList<SubCategoryModel>()
    lateinit var subcategoryAdapter: SubCategoryAdapter
    lateinit var subcategoryInterface: SubCategoryInterface
    var subcategoryModel = SubCategoryModel()
    val db = Firebase.firestore
    var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subcategoryModel = it.getSerializable("SubCategory") as SubCategoryModel
            isUpdate = it.getBoolean("isUpdate", false)
        }
        var imagePermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            }
        var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            System.out.println("in PickImage $it")
            binding.iv.setImageURI(it)
            it?.let { it1 ->
                storageRef.getReference(Calendar.getInstance().timeInMillis.toString())
                    .putFile(it1)
                    .addOnSuccessListener { uploadTask -> }
            }
        }
//        binding.btnAdd.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    requireActivity(),
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                pickImage.launch("image/*")
//            } else {
//                imagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//            }
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubCategoryAddBinding.inflate(layoutInflater)
        binding.btnAdd.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                binding.etName.error = "Enter SubCategory"
            } else {
                if(isUpdate == false) {
                    val subcategoryModel = SubCategoryModel(name = binding.etName.text.toString())
                    db.collection("SubCategory")
                        .add(subcategoryModel)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "ADD", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.e("TAG", "Failure ${it}")
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                else{
                    subcategoryModel.name = binding.etName.text.toString()
                    db.collection("SubCategory").document(subcategoryModel.key?:"")
                        .set(subcategoryModel)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "Data updated", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.e("TAG", "Failure ${it}")
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                //findNavController().navigate(R.id.subCategories)
            }
        }
        binding?.btnDelete?.setOnClickListener {
            db.collection("SubCategory")
                .document(subcategoryModel.key?:"").delete()
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
         * @return A new instance of fragment SubCategoryAdd.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubCategoryAdd().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}