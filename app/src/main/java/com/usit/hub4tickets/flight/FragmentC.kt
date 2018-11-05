package com.usit.hub4tickets.flight


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R


class FragmentC : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment, container, false
        )

        /**The below code was when the ListView was used in place of RecyclerView.  */

        /*View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 3A");
        stringList.add("Item 3B");
        stringList.add("Item 3C");
        stringList.add("Item 3D");
        stringList.add("Item 3E");
        stringList.add("Item 3F");
        stringList.add("Item 3G");
        stringList.add("Item 3H");
        stringList.add("Item 3I");
        stringList.add("Item 3J");
        stringList.add("Item 3K");
        stringList.add("Item 3L");
        stringList.add("Item 3M");
        stringList.add("Item 3N");
        stringList.add("Item 3O");
        stringList.add("Item 3P");
        stringList.add("Item 3Q");
        stringList.add("Item 3R");
        stringList.add("Item 3S");
        stringList.add("Item 3T");
        stringList.add("Item 3U");
        stringList.add("Item 3V");
        stringList.add("Item 3W");
        stringList.add("Item 3X");
        stringList.add("Item 3Y");
        stringList.add("Item 3Z");

        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = resources.getStringArray(R.array.tab_C)
        val adapter = RecyclerViewAdapter(items)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}