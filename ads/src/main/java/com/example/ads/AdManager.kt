package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton // Import Singleton nếu bạn sử dụng nó

@Singleton // Nên có nếu bạn muốn đây là một singleton được inject bởi Hilt
class AdManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private val interstitialAdUnitId = "ca-app-pub-2678727679739567/7638740222" // TEST Interstitial Ad Unit ID

    fun initialize() {
        MobileAds.initialize(context) {}
        Log.d("AdManager", "Google Mobile Ads SDK initialized.")
    }

    // Tải quảng cáo Interstitial
    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, interstitialAdUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("AdManager", "Interstitial Ad failed to load: ${adError.message}")
                interstitialAd = null
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                Log.d("AdManager", "Interstitial Ad loaded successfully.")
                interstitialAd = ad
                // Gọi hàm setupCallbacks ngay sau khi quảng cáo được tải
                // Ở đây chúng ta sẽ pass một callback rỗng,
                // callback thực sự sẽ được xử lý khi showInterstitialAd được gọi.
                // Hàm setupInterstitialAdCallbacks() sẽ được sửa để nhận callback đó.
                setupInterstitialAdCallbacks(null) // Pass null hoặc một lambda mặc định nếu không có callback cụ thể lúc load
            }
        })
    }

    // --- HÀM MỚI ĐƯỢC THÊM VÀ SỬA ĐỔI ---
    private fun setupInterstitialAdCallbacks(onAdDismissed: (() -> Unit)?) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d("AdManager", "Interstitial Ad clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d("AdManager", "Interstitial Ad dismissed.")
                interstitialAd = null // Đảm bảo đặt null để không hiển thị lại quảng cáo cũ
                loadInterstitialAd() // Tải quảng cáo tiếp theo ngay lập tức
                onAdDismissed?.invoke() // Gọi callback được truyền vào nếu nó không null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d("AdManager", "Interstitial Ad failed to show: ${adError.message}")
                interstitialAd = null
                onAdDismissed?.invoke() // Gọi callback dù quảng cáo lỗi
            }

            override fun onAdImpression() {
                Log.d("AdManager", "Interstitial Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdManager", "Interstitial Ad showed.")
            }
        }
    }
    // --- KẾT THÚC HÀM MỚI ---


    // Hiển thị quảng cáo Interstitial
    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit) {
        if (interstitialAd != null) {
            // Thiết lập callback ở đây, để đảm bảo nó đúng với callback của lần hiển thị hiện tại
            setupInterstitialAdCallbacks(onAdDismissed)
            interstitialAd?.show(activity)
        } else {
            Log.d("AdManager", "Interstitial Ad is not ready.")
            onAdDismissed() // Gọi callback ngay lập tức nếu quảng cáo không sẵn sàng
            loadInterstitialAd() // Thử tải lại
        }
    }

    // Phương thức để tạo và trả về AdView cho Banner (nếu muốn quản lý qua code thay vì XML)
    fun createBannerAdView(context: Context, adUnitId: String, adSize: AdSize = AdSize.BANNER): AdView {
        val adView = AdView(context)
        adView.setAdSize(adSize)
        adView.adUnitId = adUnitId
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return adView
    }
}