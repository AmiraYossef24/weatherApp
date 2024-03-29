package splashscreen.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R

class SplashFragment : Fragment() {

    private val SPLASH_DELAY_MS: Long = 5000
    lateinit var welAnim  : LottieAnimationView
    lateinit var loadingAnim  :LottieAnimationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.homeFragment)
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
        welAnim=view.findViewById(R.id.start)
        loadingAnim=view.findViewById(R.id.loading)

        welAnim.setAnimation(R.raw.startanim)
        loadingAnim.setAnimation(R.raw.loading)
        welAnim.playAnimation()
        loadingAnim.playAnimation()


    }


}




