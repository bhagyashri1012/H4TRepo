package com.usit.hub4tickets.flight


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R

class FragmentB : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment, container, false
        )

        /**The below code was when the ListView was used in place of RecyclerView.  */

        /*View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 2A");
        stringList.add("Item 2B");
        stringList.add("Item 2C");
        stringList.add("Item 2D");
        stringList.add("Item 2E");
        stringList.add("Item 2F");
        stringList.add("Item 2G");
        stringList.add("Item 2H");
        stringList.add("Item 2I");
        stringList.add("Item 2J");
        stringList.add("Item 2K");
        stringList.add("Item 2L");
        stringList.add("Item 2M");
        stringList.add("Item 2N");
        stringList.add("Item 2O");
        stringList.add("Item 2P");
        stringList.add("Item 2Q");
        stringList.add("Item 2R");
        stringList.add("Item 2S");
        stringList.add("Item 2T");
        stringList.add("Item 2U");
        stringList.add("Item 2V");
        stringList.add("Item 2W");
        stringList.add("Item 2X");
        stringList.add("Item 2Y");
        stringList.add("Item 2Z");

        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = resources.getStringArray(R.array.tab_B)
        val adapter = RecyclerViewAdapter(items)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}