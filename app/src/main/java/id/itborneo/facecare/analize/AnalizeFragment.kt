package id.itborneo.facecare.analize

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import id.itborneo.facecare.R
import id.itborneo.facecare.result.ResultActivity


class AnalizeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analize, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton(view)

    }

    private fun initButton(view: View) {
        view.findViewById<Button>(R.id.btn_analize).setOnClickListener {
            ResultActivity.getInstance(requireContext())
        }
    }


}