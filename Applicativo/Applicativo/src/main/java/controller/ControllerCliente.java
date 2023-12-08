package controller;

import model.*;
import postgresqlDAO.ClienteDB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import unnamed.Pair;

/**
 * The type Controller.
 */
public class ControllerCliente {
    private ClienteDB clienteDB;
    private Cliente cliente;
    private HashMap<Integer, Porto> porti;
    private HashMap<String, Compagnia> compagnie;
    private HashMap<Pair, CorsaSpecifica> corse;
    private ArrayList<Biglietto> bigliettiAcquistati;

    /**
     * Instantiates a new Controller cliente.
     */
    public ControllerCliente() {
        clienteDB = new ClienteDB();
        porti = new HashMap<>();
        compagnie = new HashMap<>();
        corse = new HashMap<Pair, CorsaSpecifica>();
        bigliettiAcquistati = new ArrayList<>();
    }

    /**
     * Prende login e password dalla GUI,
     * richiede una connessione al DB,
     * se il cliente esiste nel DB,
     * lo costruisce nel model e gli associa i suoi veicoli
     * Restituisce un valore di controllo alla GUI,
     * che si occupera' di cambiare schermata
     *
     * @param login    the login
     * @param password the password
     * @return the boolean
     */
    public boolean clienteAccede(String login, String password) {
        boolean exists = clienteDB.accedi(login, password);
        if (exists) {
            String nome = null;
            String cognome = null;
            ArrayList<String> veicoliTipo = new ArrayList<>();
            ArrayList<String> veicoliTarga = new ArrayList<>();
            //crea oggetto cliente e lo popola con i dati del DB
            clienteDB.fetchCliente(login, nome, cognome);
            clienteDB.fetchVeicoliCliente(login, veicoliTipo, veicoliTarga);
            for (int i = 0; i < veicoliTarga.size(); i++) {
                cliente.addVeicolo(new Veicolo(veicoliTipo.get(i), veicoliTarga.get(i)));
            }
            cliente = new Cliente(login, password, nome, cognome);

            //crea il resto del contorno di cliente
            buildModel();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Questo metodo si occupa di costruire l'ambiente dell'applicativo a partire dal fetching del DB
     */
    public void buildModel() {
        buildCompagnie();
        buildPorti();
        buildCorse();
        buildBigliettiAcquistati();
    }

    /**
     * Download porti.
     */
    public void buildPorti() {
        ArrayList<Integer> idPorto = new ArrayList<>();
        ArrayList<String> comuni = new ArrayList<>();
        ArrayList<String> indirizzi = new ArrayList<>();
        ArrayList<String> numeriTelefono = new ArrayList<>();
        clienteDB.fetchPorti(idPorto, comuni, indirizzi, numeriTelefono);
        for (int i = 0; i < comuni.size(); i++) {
            porti.put(idPorto.get(i), new Porto(idPorto.get(i), comuni.get(i), indirizzi.get(i), numeriTelefono.get(i)));
        }
    }

    /**
     * Build compagnie.
     */
    public void buildCompagnie() {
        //password verra' settata a null nel model per motivi di sicurezza
        ArrayList<String> login = new ArrayList<>();
        ArrayList<String> nomeCompagnia = new ArrayList<>();
        ArrayList<String> sitoWeb = new ArrayList<>();
        //Elementi dei Social
        ArrayList<String> compagniaSocial = new ArrayList<>();
        ArrayList<String> nomeSocial = new ArrayList<>();
        ArrayList<String> tagSocial = new ArrayList<>();
        //Elementi delle Email
        ArrayList<String> compagniaEmail = new ArrayList<>();
        ArrayList<String> indirizzoEmail = new ArrayList<>();
        //Elementi dei telefoni
        ArrayList<String> compagniaTelefono = new ArrayList<>();
        ArrayList<String> numeroTelefono = new ArrayList<>();
        clienteDB.fetchCompagnie(login, nomeCompagnia, sitoWeb, compagniaSocial, nomeSocial, tagSocial, compagniaEmail, indirizzoEmail, compagniaTelefono, numeroTelefono);
        for (int i = 0; i < login.size(); i++) {
            compagnie.put(login.get(i), new Compagnia(login.get(i), nomeCompagnia.get(i), sitoWeb.get(i)));
        }

        //Aggiungo a ciascuna compagnia i suoi contatti
        for (int i = 0; i < tagSocial.size(); i++) {
            compagnie.get(compagniaSocial.get(i)).addAccount(new AccountSocial(compagnie.get(compagniaSocial.get(i)), nomeSocial.get(i), tagSocial.get(i)));
        }
        for (int i = 0; i < indirizzoEmail.size(); i++) {
            compagnie.get(compagniaEmail.get(i)).addEmail(indirizzoEmail.get(i));
        }
        for (int i = 0; i < numeroTelefono.size(); i++) {
            compagnie.get(compagniaTelefono.get(i)).addTelefono(numeroTelefono.get(i));
        }
    }

    /**
     * Questo metodo (l'ordine é dettato dalle dipendenze):
     *  1) costruisce i natanti e li assegna alle compagnie
     *  2) costruisce le corse regolari e le assegna alle compagnie
     *  3) assegna i periodi alle corse regolari
     *  4) costruisce le corse specifiche e popola la collezione 'corse' di questa classe
     */
    public void buildCorse() {
        //Elementi dei natanti
        ArrayList<String> compagniaNatante = new ArrayList<>();
        ArrayList<String> nome = new ArrayList<>();
        ArrayList<Integer> capienzaPasseggeri = new ArrayList<>();
        ArrayList<Integer> capienzaVeicoli = new ArrayList<>();
        ArrayList<String> tipo = new ArrayList<>();
        clienteDB.fetchNatanti(compagniaNatante, nome, capienzaPasseggeri, capienzaVeicoli, tipo);
        for (int i = 0; i < nome.size(); i++) {
            compagnie.get(compagniaNatante.get(i)).addNatante(new Natante(compagnie.get(compagniaNatante.get(i)), nome.get(i), capienzaPasseggeri.get(i), capienzaVeicoli.get(i), tipo.get(i)));
        }

        //Elementi delle corse regolari
        ArrayList<Integer> idCorsa = new ArrayList<>();
        ArrayList<Integer> idPortoPartenza = new ArrayList<>();
        ArrayList<Integer> idPortoArrivo = new ArrayList<>();
        ArrayList<LocalTime> orarioPartenza = new ArrayList<>();
        ArrayList<LocalTime> orarioArrivo = new ArrayList<>();
        ArrayList<Float> costoIntero = new ArrayList<>();
        ArrayList<Float> scontoRidotto = new ArrayList<>();
        ArrayList<Float> costoBagaglio = new ArrayList<>();
        ArrayList<Float> costoPrevendita = new ArrayList<>();
        ArrayList<Float> costoVeicolo = new ArrayList<>();
        ArrayList<String> compagniaCorsa = new ArrayList<>();
        ArrayList<String> nomeNatante = new ArrayList<>();
        clienteDB.fetchCorseRegolari(idCorsa, idPortoPartenza, idPortoArrivo, orarioPartenza, orarioArrivo, costoIntero, scontoRidotto, costoBagaglio, costoPrevendita, costoVeicolo, compagniaCorsa, nomeNatante);
        for (int i = 0; i < costoIntero.size(); i++) {
            int id = idCorsa.get(i);
            Compagnia c = compagnie.get(compagniaCorsa.get(i));
            Natante n = c.getNatantiPosseduti().get(nome.get(i));
            Porto pPartenza = porti.get(idPortoPartenza.get(i));
            Porto pArrivo = porti.get(idPortoArrivo.get(i));
            LocalTime oraPartenza = orarioPartenza.get(i);
            LocalTime oraArrivo = orarioArrivo.get(i);
            float cIntero = costoIntero.get(i);
            float sRidotto = scontoRidotto.get(i);
            float cBagaglio = costoBagaglio.get(i);
            float cPrev = costoPrevendita.get(i);
            float cVei = costoVeicolo.get(i);
            c.addCorsaRegolare(new CorsaRegolare(id, c, n, pPartenza, pArrivo, oraPartenza, oraArrivo, cIntero, sRidotto, cBagaglio, cPrev, cVei));
        }

        //Elementi dei periodi
        HashMap<Integer, Periodo> periodi = new HashMap<>();
        ArrayList<String> compagnia = new ArrayList<>();
        ArrayList<Integer> corsa = new ArrayList<>();
        ArrayList<Integer> idPeriodo = new ArrayList<>();
        ArrayList<Date> dataInizio = new ArrayList<>();
        ArrayList<Date> dataFine = new ArrayList<>();
        ArrayList<BitSet> giorni = new ArrayList<>();
        clienteDB.fetchPeriodiAttivitaCorse(idPeriodo, dataInizio, dataFine, giorni, corsa, compagnia);
        //Assegno un periodo alla sua corsa.
        for (int i = 0; i < dataInizio.size(); i++) {
            Compagnia c = compagnie.get(compagnia.get(i));
            CorsaRegolare cr = c.getCorseErogate().get(corsa.get(i));
            cr.addPeriodoAttivita(new Periodo(idPeriodo.get(i), dataInizio.get(i), dataFine.get(i), giorni.get(i)));
        }

        //Elementi delle corseSpecifiche
        ArrayList<String> compagniaCorsaS = new ArrayList<>();
        ArrayList<Integer> corsaRegolare = new ArrayList<>();
        ArrayList<LocalDate> data = new ArrayList<>();
        ArrayList<Integer> postiDispPass = new ArrayList<>();
        ArrayList<Integer> postiDispVei = new ArrayList<>();
        ArrayList<Integer> minutiRitardo = new ArrayList<>();
        ArrayList<Boolean> cancellata = new ArrayList<>();
        clienteDB.fetchCorseSpecifiche(compagniaCorsaS, corsaRegolare, data, postiDispPass, postiDispVei, minutiRitardo, cancellata);
        for (int i = 0; i < data.size(); i++) {
            Compagnia c = compagnie.get(compagniaCorsaS.get(i));
            CorsaRegolare cr = c.getCorseErogate().get(corsaRegolare.get(i));
            LocalDate d = data.get(i);
            int pPass = postiDispPass.get(i);
            int pVei = postiDispVei.get(i);
            int mRit = minutiRitardo.get(i);
            boolean canc = cancellata.get(i);
            Pair pair = new Pair(cr.getIdCorsa(), d);
            corse.put(pair, new CorsaSpecifica(cr, d, pPass, pVei, mRit, canc));
        }
    }

    /**
     * Build biglietti acquistati.
     */
    public void buildBigliettiAcquistati() {
        ArrayList<Integer> idBiglietto = new ArrayList<>();
        ArrayList<Integer> idCorsa = new ArrayList<>();
        ArrayList<LocalDate> dataCorsa = new ArrayList<>();
        ArrayList<String> targaVeicolo = new ArrayList<>();
        ArrayList<Boolean> prevendita = new ArrayList<>();
        ArrayList<Boolean> bagaglio = new ArrayList<>();
        ArrayList<Float> prezzo = new ArrayList<>();
        ArrayList<LocalDate> dataAcquisto = new ArrayList<>();
        ArrayList<Integer> etaPasseggero = new ArrayList<>();
        clienteDB.fetchBigliettiCliente(cliente.getLogin(), idBiglietto, idCorsa, dataCorsa, targaVeicolo, prevendita, bagaglio, prezzo, dataAcquisto, etaPasseggero);
        for (int i = 0; i < idBiglietto.size(); i++) {
            //trovo la corsa specifica con id e Data
            CorsaSpecifica cs = corse.get(new Pair(idCorsa.get(i), dataCorsa.get(i)));
            Veicolo v = cliente.getVeicoliPosseduti().get(targaVeicolo.get(i));

            cliente.addBiglietto(new Biglietto(idBiglietto.get(i), cliente, cs, etaPasseggero.get(i), v, bagaglio.get(i), prevendita.get(i), prezzo.get(i), dataAcquisto.get(i)));
        }
    }

    /**
     * Visualizza corse.
     *
     * @param portoPartenza the porto partenza
     * @param portoArrivo   the porto arrivo
     * @param data          the data
     * @param etaPasseggero the eta passeggero
     * @param veicolo       the veicolo
     * @param bagaglio      the bagaglio
     */
    public void visualizzaCorse(Porto portoPartenza, Porto portoArrivo, Date data, int etaPasseggero, boolean veicolo, boolean bagaglio) {
        ArrayList<Pair> corseSelezionate = new ArrayList<>();

        //un iteratore per scorrere la HashMap
        for (Map.Entry<Pair, CorsaSpecifica> it : corse.entrySet()) {
            CorsaSpecifica cs = it.getValue();
            if (cs.getCorsaRegolare().getPortoPartenza().equals(portoPartenza) && cs.getCorsaRegolare().getPortoArrivo().equals(portoArrivo) && cs.getData().equals(data)) {
                float prezzoCalcolato = cs.getCorsaRegolare().getCostoIntero();
                if (etaPasseggero < 12) {
                    prezzoCalcolato *= cs.getCorsaRegolare().getScontoRidotto();
                } else if (veicolo == true) { //si suppone che la GUI abbia giá verificato che un minorenne non sia associato ad un veicolo!
                    prezzoCalcolato += cs.getCorsaRegolare().getCostoVeicolo();
                } else if (bagaglio == true) {
                    prezzoCalcolato += cs.getCorsaRegolare().getCostoBagaglio();
                }
                corseSelezionate.add(new Pair(cs, prezzoCalcolato));
            }
        }
        //ADESSO STAMPA corseSelezionate SULLA GUI GLI ARRAYLIST IN UNA OPPORTUNA FORMATTAZIONE
    }

    /**
     * Acquista biglietto.
     *
     * @param cs            the cs
     * @param v             the v
     * @param prevendita    the prevendita
     * @param bagaglio      the bagaglio
     * @param etaPasseggero the eta passeggero
     * @param prezzo        the prezzo
     */
    public void acquistaBiglietto(CorsaSpecifica cs, Veicolo v, boolean prevendita, boolean bagaglio, int etaPasseggero, float prezzo) {
        clienteDB.acquistaBiglietto(cs.getCorsaRegolare().getIdCorsa(), cs.getData(), cliente.getLogin(), v.getTarga(), prevendita, bagaglio, prezzo, LocalDate.now(), etaPasseggero);
        //DISCUSSIONE SUGLI ID DEI BIGLIETTI APPENA ACQUISTATI (vedi immediatamente sotto, primo parametro)
        cliente.addBiglietto(new Biglietto(-1, cliente, cs, etaPasseggero, v, prevendita, bagaglio, prezzo, LocalDate.now()));
    }

    /**
     * Visualizza porti.
     */
    public void visualizzaPorti() {
        //QUI STAMPA porti SULLA GUI IN MANIERA OPPORTUNA
    }
}