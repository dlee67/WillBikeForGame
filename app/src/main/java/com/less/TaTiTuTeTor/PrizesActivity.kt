package com.less.TaTiTuTeTor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.beust.klaxon.Klaxon
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject


class PrizesActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var prizesAdapter: RecyclerView.Adapter<PrizesAdapter.PrizeHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prizes_view)

        // Just like the other Firebase services, I need the reference to do anything on it.
        // The below apparently fetches me the entire Real Time Database?
        database = FirebaseDatabase.getInstance().getReference()

        recyclerView = findViewById(R.id.prizes_recycler_view)
        layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        populatePrizes()

    }

    fun populatePrizes(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var newPrize: Prize
                var prizeList = ArrayList<Prize>()
                // So, the getValue() returns an object, and the documentation
                // has a list of objects it could return.
                // https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot.html#getValue(boolean)
                val value = dataSnapshot.getValue() as List<Map<*, *>>
                for(prize in value){
                    newPrize = Prize()
                    newPrize.prizeName = prize.get("prizeName").toString()
                    newPrize.requiredHours = prize.get("requiredHours").toString()
                    prizeList.add(newPrize)
                }
                recyclerView.setAdapter(PrizesAdapter(prizeList))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("dhl", "" + error.message)
            }
        })
    }
}