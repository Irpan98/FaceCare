package id.itborneo.facecare.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import id.itborneo.facecare.R
import id.itborneo.facecare.utils.ARG_PARAM1
import id.itborneo.facecare.utils.ARG_PARAM2
import id.itborneo.facecare.utils.ARG_PARAM3

class OnBoardingContentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imageBackground: Int? = null
    private var titleText: String? = null
    private var subText: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageBackground = it.getInt(ARG_PARAM1)
            titleText = it.getString(ARG_PARAM2)
            subText = it.getString(ARG_PARAM3)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_content, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivOnBoarding = view.findViewById<ImageView>(R.id.ivOnBoarding)

        Glide.with(requireContext())
            .load(imageBackground)
            .into(ivOnBoarding)

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitle)

        tvTitle.text = titleText
        tvSubTitle.text = subText
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param imageBackground Parameter 1.
         * @param title Parameter 2.
         * @return A new instance of fragment OnBoardingContentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(imageBackground: Int, title: String, subtitle: String) =
            OnBoardingContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, imageBackground)
                    putString(ARG_PARAM2, title)
                    putString(ARG_PARAM3, subtitle)
                }
            }
    }
}