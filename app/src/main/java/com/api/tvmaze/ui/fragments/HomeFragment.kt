package com.api.tvmaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.R
import com.api.tvmaze.ui.adapter.HomeListAdapter
import com.api.tvmaze.api.Network
import com.api.tvmaze.api.SearchAPI
import com.api.tvmaze.api.ShowAPI
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: 28/04/21 Oi Jéssica, eu fiz algumas considerações sobre o código para
//  que fique mais performático e esteja seguindo as melhores práticas.
class HomeFragment : Fragment() {

    companion object {
        const val URL = "https://api.tvmaze.com"
    }

    val retrofitClient = Network.retrofitConfig(URL)

    private var showList = arrayListOf<Show>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getShowAPI()

    }

    // FIXME: 28/04/21 Então, uma das considerações que eu tenho a fazer é: Como o projeto está usando view model,
    //  entende-se que a arquitetura do projeto é MVVM (Model-View-ViewModel).
    //  Com isso, não é recomendado tratar requisições ou coisas do tipo na View.
    //  Essas funções que estão relacionadas a API devem estar no ViewModel.
    fun getShowAPI() {
        val call = retrofitClient.create(ShowAPI::class.java).getShowAPI()

        call.enqueue(
            object : Callback<List<Show>> {

                override fun onResponse(call: Call<List<Show>>, response: Response<List<Show>>) {

                    // FIXME: 28/04/21 A partir do debug, eu notei que os atributos estão nulos
                    val shows = response.body()

                    shows?.let {

                        val rvHome = view?.findViewById<RecyclerView>(R.id.rvHome)
                        rvHome?.let { rvHome.layoutManager = GridLayoutManager(context, 2) }

                        val homeListAdapter = HomeListAdapter(shows, requireActivity())
                        rvHome?.adapter = homeListAdapter

                        val loading = view?.findViewById<ProgressBar>(R.id.progressBarHome)
                        loading?.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Show>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // FIXME: 28/04/21 Essa função também deveria estar no viewmodel. Uma outra coisa que eu notei
    //  é que você está usando a Thread principal. Requisições, operações em banco de dados devem ser realizadas em
    //  uma Thread separada ou pode travar o app e causar um ANR.
    private fun getSearchResult(query: String) {
        val call2 = retrofitClient.create(SearchAPI::class.java).getShowSearchAPI(query)

        call2.enqueue(
            object : Callback<List<Search>> {

                override fun onResponse(
                    call: Call<List<Search>>,
                    response: Response<List<Search>>
                ) {

                    // FIXME: 28/04/21 A partir do debug, eu notei que os atributos estavam nulos. Ao verificar o JSON
                    //  percebi que na busca existe um objeto chamado show e como antes estava retornando um objeto que
                    //  não tinha um atributo com nome em específico ele não conseguia identifica-lo no json do search e
                    //  por isso retornou null. Eu fiz o ajuste e tá funcionando.
                    val shows = response.body()

                    shows?.let {

                        // FIXME: 28/04/21 Eu não faria assim pois, está recriando várias vezes o recyclerview.
                        //  Eu colocaria essas 2 de baixo no onViewCreated. Além disso, o layout manager e o spanCount
                        //  podem ser definidos no xml.
                        val rvHome = view?.findViewById<RecyclerView>(R.id.rvHome)
                        rvHome?.let { rvHome.layoutManager = GridLayoutManager(context, 2) }

                        // FIXME: 28/04/21 Aqui foi um workaround. Você pode criar um método updateList no adapter que vai
                        //  atualizar a lista, numa lógica similar. Limpa a lista anterior, adiciona todos os itens na lista
                        //  e chama o notifyOnDataSetChange
                        if (showList.isNotEmpty()) {
                            showList.clear()
                        }

                        for (show in shows) {
                            showList.add(show.show)
                        }

                        val homeListAdapter = HomeListAdapter(showList, requireActivity())
                        rvHome?.adapter = homeListAdapter

                        val loading = view?.findViewById<ProgressBar>(R.id.progressBarHome)
                        loading?.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Search>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // FIXME: 28/04/21 Para evitar possíveis null pointer com o view, eu usária o requireView().
        //  Além disso, utilizaria o apply, tipo asssim: requireView().apply{val imageSearch = findViewById...}.
        //  Essa é uma forma mais organizada de escrever o código em koltin. Uma alternativa para evitar o findViewById
        //  é o viewBinding que é uma biblioteca do jetpack.
        requireView().apply {
            val imageSearch = findViewById<ImageView>(R.id.img_search)
            val editSearch = findViewById<EditText>(R.id.edt_search)

            imageSearch?.setOnClickListener {

                val callSearch = editSearch?.text.toString()

                getSearchResult(callSearch)

            }
        }

    }
}

