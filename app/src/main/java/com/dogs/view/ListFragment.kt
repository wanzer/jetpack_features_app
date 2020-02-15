package com.dogs.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dogs.R
import com.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_dogs_list.*

class ListFragment : Fragment() {

//    val action = ListFragmentDirections.actionListFragmentToDetailFragment()
//    action.dogId = 5
//    Navigation.findNavController(it).navigate(action)

    lateinit var viewModel: ListViewModel
    var dogsListAdapter = DogsListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_dogs_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_container.setOnRefreshListener {
            dogs_list.visibility = View.GONE
            error_notification.visibility = View.GONE
            load_progress.visibility = View.VISIBLE
            viewModel.refreshByPassCash()
            swipe_container.isRefreshing = false
        }

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.updateDogsData()

        updateViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                view?.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToSettingsFragment()) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateViewModel(){
        dogs_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        viewModel.dogs.observe(this, Observer {it ->
            it?.let {
                dogs_list.visibility = View.VISIBLE
                dogsListAdapter.updateDogsList(it)
            }
        })

        viewModel.loading.observe(this, Observer {
            it?.let {
                load_progress.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    dogs_list.visibility = View.GONE
                    error_notification.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(this, Observer { it->
            it?.let {
                error_notification.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}