package com.frantzoe.stock;

import java.util.ArrayList;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private static final int NOMBRE_PAGES = 2;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Adapter du ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        //Marge entre les pages
        viewPager.setPageMargin(20);
        //Récupération de la barre d'action
        actionbar = getSupportActionBar();
        actionbar.setTitle(getString(R.string.titre_section1));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                //Pour changer le titre du ActionBar quand on passe d'un fragment à l'autre
                switch (i) {
                    case 0:
                        actionbar.setTitle(getString(R.string.titre_section1));
                        break;
                    case 1:
                        actionbar.setTitle(getString(R.string.titre_section2));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Définition de l'adapter pour les fragments du ViewPager : peut aussi être créé dans une autre page dédiée.
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            //Affectation des pages aux fragments
            switch (position) {
                case 0:
                    //Dans le cas où c'est la page 'Ajouter'
                    return AjouterFragment.newInstance(position + 1);
                case 1:
                    //Dans le cas où c'est la page 'Afficher'
                    return AfficherFragment.newInstance(position + 1);
                //Les sections définies commencent par 1 et non 0 donc on rajoute 1 à la position
            }
            return null;
        }

        @Override
        public int getCount() {
            // Nombre de pages à afficher
            return NOMBRE_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.titre_section1).toUpperCase();
                case 1:
                    return getString(R.string.titre_section2).toUpperCase();
            }
            return null;
        }
    }

    //Fragment pour l'interface ajouter : peut aussi être créé dans une autre page dédiée.
    public static class AjouterFragment extends Fragment {

        private static final String NUMERO_SECTION = "numero_section";
        private EditText etLibelle;
        private EditText etQuantite;
        private Button btAjouter;
        private MaBDD mabdd;

        public static AjouterFragment newInstance(int numeroSection) {
            AjouterFragment fragment = new AjouterFragment();
            Bundle args = new Bundle();
            args.putInt(NUMERO_SECTION, numeroSection);
            fragment.setArguments(args);
            return fragment;
        }

        public AjouterFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_ajouter, container, false);
            //Instantiation de la BDD
            this.mabdd = new MaBDD(getActivity());
            this.etLibelle = (EditText) view.findViewById(R.id.etLibelle);
            this.etQuantite = (EditText) view.findViewById(R.id.etQuantite);
            this.btAjouter = (Button) view.findViewById(R.id.btAjouter);
            //Désactivation du bouton 'Ajouter' au lancement
            this.btAjouter.setEnabled(false);
            this.btAjouter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAjouterClick(v);
                }
            });
            this.etLibelle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etQuantite.length() != 0 && etLibelle.length() != 0) {
                        btAjouter.setEnabled(true);
                    }
                    else {
                        btAjouter.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            this.etQuantite.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etQuantite.length() != 0 && etLibelle.length() != 0) {
                        btAjouter.setEnabled(true);
                    }
                    else {
                        btAjouter.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            return view;
        }

        public void onAjouterClick(View view) {
            String libelle = etLibelle.getText().toString();
            Produit produit = new Produit(libelle, Integer.parseInt(etQuantite.getText().toString()));
            mabdd.ajouterProduit(produit);
            Toast.makeText(getActivity(), libelle + " ajouté!", Toast.LENGTH_SHORT).show();
            etLibelle.setText("");
            etQuantite.setText("");
            etLibelle.requestFocus();
            //Récupération du fragment AfficherFragment situé en deuxième position dans la liste getFragments() avec get(1)
            //Appel à la methode rafraichirList(view) pour rafraichir la liste au click du bouton 'Ajouter'
            ((AfficherFragment) getActivity().getSupportFragmentManager().getFragments().get(1)).rafraichirList(view);
        }
    }

    //Fragment pour l'interface afficher : peut aussi être créé dans une autre page dédiée.
    public static class AfficherFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private ListView lvProduit;
        private RadioGroup radioGroup;
        private ArrayAdapter<String> adapter;
        private ArrayList<String> produits;
        private MaBDD mabdd;

        public static AfficherFragment newInstance(int sectionNumber) {
            AfficherFragment fragment = new AfficherFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public AfficherFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_afficher, container, false);
            //Instantiation de la BDD
            this.mabdd = new MaBDD(getActivity());
            this.produits = new ArrayList<>();
            this.radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
            this.lvProduit = (ListView) view.findViewById(R.id.lvProduit);
            this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, android.R.id.text1, produits);
            this.lvProduit.setAdapter(adapter);
            this.lvProduit.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //Methode pour supprmes un produit de la BDD
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    mabdd.supprimerProduit(textView.getText().toString());
                    produits.remove(position);
                    Toast.makeText(getActivity(), "Supprimé!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    return true;
                }
            });
            this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    rafraichirList(view);
                }
            });
            //Charger la liste au  démarrage
            onAfficherClick(view);
            return view;
        }

        //Méthode pour afficher tous les produits de la liste
        public void onAfficherClick(View view) {
            adapter.clear();
            for (Produit produit : mabdd.tousLesProduits()) {
                this.produits.add(produit.get_libelle());
            }
            //Notifier l'adapter des changements
            adapter.notifyDataSetChanged();
        }

        //Méthode pour afficher les produits en rupture de la liste
        public void onRuptureClick(View view) {
            ArrayList<Produit> lesProduits = mabdd.tousLesProduits();
            adapter.clear();
            for (Produit produit : lesProduits) {
                if (produit.get_quantite() == 0) {
                    this.produits.add(produit.get_libelle());
                }
            }
            //Notifier l'adapter des changements
            adapter.notifyDataSetChanged();
        }

        //Méthode pour methode pour rafraichir la liste lors d'une MAJ ou du click d'un bouton radio
        public void rafraichirList(View view) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radioAfficher:
                    onAfficherClick(view);
                    break;
                case R.id.radioRupture:
                    onRuptureClick(view);
                    break;
            }
        }
    }
}
