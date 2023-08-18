package be.nicolas.road_trip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import be.nicolas.road_trip.adapter.MapAdapter
import be.nicolas.road_trip.databinding.ActivityMainBinding
import be.nicolas.road_trip.fragments.FragmentFavori
import be.nicolas.road_trip.fragments.FragmentMenu
import be.nicolas.road_trip.fragments.FragmentHome
import be.nicolas.road_trip.fragments.FragmentMeteo
import be.nicolas.road_trip.fragments.FragmentPoi
import be.nicolas.road_trip.models.CityModel
import be.nicolas.road_trip.models.MeteoModel
import be.nicolas.road_trip.services.NominatimRequest
import be.nicolas.road_trip.services.OpenWeather
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), FragmentMenu.OnMenuclickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MapAdapter
    private val villeTrouvee: MutableList<CityModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LoadFragmentMenu()
        setupSearchBar()

        adapter = MapAdapter(this, villeTrouvee)

        binding.lvMain.adapter = adapter
        selectionDeLaVille()
    }

    //region Barre de recherche

    /** On crée une fonction qui initialise la recherche et que l'on paramètre en fonction de nos besoin
     * ici on va lui demander d'attendre que l'utilisateur ait taper 3 caractères avant de commencer la rechercher
     * on va également prendre en compte que l'utilisateur puisse écrire vite et donc mettre un delais afin qu'il ait fini de taper
     * ensuite on va faire la requête vers l'api qui se trouve dans les services.
     **/
    private fun setupSearchBar() {

        binding.svMain.editText.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> if (hasFocus){
                binding.fragmentMeteo.visibility = View.GONE
            }
            }

        // On "écoute" le binding de la searchView et vérifie que l'utilisateur tape quelque chose sur son clavier
        binding.svMain.setOnMenuItemClickListener { true }
        // On "écoute" toujours le binding en précisant se que l'on écoute, de plus on lui indique d'utiliser la fonction debounce qu'on a crée
        binding.svMain
            .editText
            .addTextChangedListener { debounceSearch() }
    }

    // On crée une variable qui va enregistrer se qui va être taper par l'utilisateur
    private var searchMemory: String? = null

    // Création d'une fonction de retard à la touche, pour permettre à l'utilisateur de taper rapidement sans déclencher la recherche
    private fun debounceSearch() {
        // On crée une variable (searchCurrent) qui reprend le texte avec le binding du SearchView
        val searchCurrent = binding.svMain.text.toString()
        // Si (if) la variable searchCurrent est plus petit que 3 caractères OU que searchCurrent est égale à searchMemory alors on return et on ne fait pas la recherche
        if (searchCurrent.length < 3 || searchCurrent == searchMemory)
            return
        // Sinon searchMemory prend la valeur de searchCurrent
        searchMemory = searchCurrent
        // On Utilise la coroutine lifecycleScope pour initialiser le delais de frappe
        lifecycleScope.launch {
            delay(500)
            // Si (if) searchCurrent est différent de searchMemory alors on return sur le lifecycleScope
            if (searchCurrent != searchMemory)
                return@launch
            // Enfin on indique que la fonction processSearch qu'on a crée prend la propriété de la variable searchCurrent
            processSearch(searchCurrent)
        }
    }

    // On Crée une fonction
    private fun processSearch(searchValue: String) {

        val nominatimRequest = NominatimRequest(this, lifecycleScope)

        nominatimRequest.rechercheParVille(
            searchValue,
            object : NominatimRequest.OnNominatimRequestResultListener {
                override fun onNominatimSuccess(cities: List<CityModel>) {
                    listeDeRecherche(cities)
                }
                override fun onNominatimError(message: String) {
                    Toast.makeText(applicationContext, "Error test4", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun listeDeRecherche(cities: List<CityModel>) {

        villeTrouvee.clear()
        villeTrouvee.addAll(cities)
        adapter.notifyDataSetChanged()
    }

    private fun selectionDeLaVille(){

        binding.lvMain.setOnItemClickListener{ parent, View, position, id ->
            val villeChoisie = parent.getItemAtPosition(position) as CityModel
            Toast.makeText(this, "$villeChoisie", Toast.LENGTH_LONG).show()
            // Affiche le fragment météo
            binding.fragmentMeteo.visibility = View.visibility
            // Obtenir le fragment actuel
            binding.svMain.hide()
            // Affiche ou non la recherche précédente
            //villeTrouvee.clear()
            adapter.notifyDataSetChanged()
            val fragmentActuel = supportFragmentManager.findFragmentById(R.id.Main_Content) as? FragmentHome

            if (fragmentActuel !is FragmentHome){
                // TODO: revenir sur le FragmentHome
            }

            fragmentActuel!!.villeSurLaCarte(villeChoisie)
            rechercherMeteoVille(villeChoisie)
        }
    }

    private fun rechercherMeteoVille(ville: CityModel) {
        Log.d("rechercherMeteoVille 1 ",ville.toString())
        OpenWeather(this, lifecycleScope).meteoDulieu(
            ville,
            object : OpenWeather.OnReponseListener{
                override fun onRequestReponseSucces(result: MeteoModel) {
                    Log.d("rechercherMeteoVille 2 ",result.toString())
                    afficheMeteo(result)
                }

                override fun onRequestReponseError(message: String?) {
                    Log.d("rechercherMeteoVille 3 ",message.toString())
                }
            }
        )
    }

    private fun afficheMeteo(result: MeteoModel) {
        val fragmentMeteo = FragmentMeteo.newInstance(result)

        Log.d("rechercherMeteoVille 4 ", "La fragment c'est trop cool :o")

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_meteo, fragmentMeteo)
        }
    }
    /* TODO Reaction au clique sur la liste
        - Fermer la liste des resultats
        - En fonction de la page :
            • Si on est sur la Home → Récuperer le FragmentHome via le supportFragmentManager
               /!\ Ne pas créer un nouveau fragment !!!!!!!!!!!!!!!!
            • Si on est sur un autre fragment → Afficher le FragmentHome
        - Envoi au FragmentHome les données de la ville
    */


    //endregion


    //region Fragment Menu

    private fun LoadFragmentMenu() {
        val menu: FragmentMenu = FragmentMenu()
        menu.setOnMenuClickListener(this)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.Menu_Bar, menu)
            add(R.id.Main_Content, FragmentHome())
        }
    }

    override fun onMenuClick(selectedMenu: FragmentMenu.SelectedMenu) {
        when (selectedMenu) {
            FragmentMenu.SelectedMenu.HOME -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.Main_Content, FragmentHome())
                }
            }

            FragmentMenu.SelectedMenu.POI -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    binding.fragmentMeteo.visibility = View.GONE
                    replace(R.id.Main_Content, FragmentPoi())
                }
            }

            FragmentMenu.SelectedMenu.FAV -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    binding.fragmentMeteo.visibility = View.GONE
                    replace(R.id.Main_Content, FragmentFavori())
                }
            }
        }
    }
    //endregion


}






