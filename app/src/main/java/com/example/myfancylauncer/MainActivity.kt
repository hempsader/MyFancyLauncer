package com.example.myfancylauncer

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
private const val TAG = "NerdLauncherActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler = findViewById(R.id.recyclerView)
        recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = RecyclerAppsView(setupAdapter())
        }
    }

    private fun setupAdapter(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val activities = packageManager.queryIntentActivities(intent,0)
        activities.sortWith(Comparator { o1, o2 ->
            String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(packageManager).toString(),o2.loadLabel(packageManager).toString())
          })
        return activities
    }



    inner class RecyclerAppsView(val list: List<ResolveInfo>): RecyclerView.Adapter<RecyclerAppsView.ViewHolder>() {


        inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview), View.OnClickListener{
            private val text = itemview as TextView

            fun bind(app: ResolveInfo){
                text.setOnClickListener(this)
                text.text = app.loadLabel(packageManager)
            }

            override fun onClick(v: View?) {
                startApp()
            }
            fun startApp(){
                 val intent = Intent(Intent.ACTION_MAIN).apply {
                     setClassName(list[adapterPosition].activityInfo.applicationInfo.packageName,list[adapterPosition].activityInfo.name)
                 }
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}