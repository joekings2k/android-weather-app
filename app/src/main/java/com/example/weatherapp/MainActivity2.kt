package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.models.Clouds

class MainActivity2 : AppCompatActivity() {

    private lateinit var iv1:ImageView
    private lateinit var iv2:ImageView
    private lateinit var iv3:ImageView
    private lateinit var iv4:ImageView
    private lateinit var iv5:ImageView



    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val usedtem: String? = intent.getStringExtra("EXTRA_TEMP")
        viewPager2= findViewById(R.id.view_pager2)
        iv1=findViewById(R.id.iv1)
        iv2=findViewById(R.id.iv2)
        iv3=findViewById(R.id.iv3)
        iv4=findViewById(R.id.iv4)
        iv5=findViewById(R.id.iv5)




        val heatclothes = listOf(R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5)
        val coldclothes = listOf(R.drawable.image6,R.drawable.image7,R.drawable.image8,R.drawable.image10,R.drawable.image11)
        var images = listOf<Int>()
        val inttemp: Double? = usedtem?.toDouble()
        if (inttemp != null) {
            if(inttemp > 16 && inttemp < 24 ){
                images = heatclothes
            } else if (inttemp < 16) {
                images = coldclothes
            }else{
                images = heatclothes
            }
        }

        val adapter =ViewPageAdapter(images)
        viewPager2.adapter = adapter

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                changeColor()
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }

        })


    }

    fun changeColor(){
        when(viewPager2.currentItem){
            0->{
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.active))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv4.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv5.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
            }
            1->{
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.active))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv4.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv5.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
            }
            2->{
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.active))
                iv4.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv5.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
            }
            3->{
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv4.setBackgroundColor(applicationContext.resources.getColor(R.color.active))
                iv5.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
            }
            4->{
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv4.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv5.setBackgroundColor(applicationContext.resources.getColor(R.color.active))
            }
        }
    }

}