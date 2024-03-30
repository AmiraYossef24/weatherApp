package splashscreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R


class InitialFragment : Fragment() {

    lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_initial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animationView=view.findViewById(R.id.initAnimationView)
        animationView.setAnimation(R.raw.snow_anim)
        animationView.playAnimation()
        val dialogFragment= MyDialogFragment()
        dialogFragment.show(childFragmentManager, "MyDialogFragment")
    }
}