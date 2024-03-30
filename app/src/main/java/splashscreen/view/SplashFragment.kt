package splashscreen.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R

class SplashFragment : Fragment() {

    private val SPLASH_DELAY_MS: Long = 5000
    lateinit var loadingAnim  :LottieAnimationView
    lateinit var welTx : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.initialFragment)
        }, SPLASH_DELAY_MS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welTx=view.findViewById(R.id.welcomeTXID)
        loadingAnim=view.findViewById(R.id.loading)
        loadingAnim.setAnimation(R.raw.loading)
        loadingAnim.playAnimation()

        val anim = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        welTx.startAnimation(anim)




    }


}




