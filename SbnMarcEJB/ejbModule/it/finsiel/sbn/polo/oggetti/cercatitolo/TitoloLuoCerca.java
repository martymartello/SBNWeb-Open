/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.finsiel.sbn.polo.oggetti.cercatitolo;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_aut_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_cla_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_mar_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_sog_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_the_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_tit_c_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vf_titolo_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_luoResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.transactionmaker.CercaTitolo;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_aut_luo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_cla_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_cla_luo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_luo_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_mar_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_mar_luo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_sog_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_sog_luo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_the_luo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_tit_c_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_tit_c_luo;
import it.finsiel.sbn.polo.orm.viste.Vf_titolo_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_luo;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementoAutLegatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;

/**
 * Classe TitoloLuoCerca
 * <p>
 * Esegue quelle ricerche di titoli che valorizzano i filtri relativi ad un autore
 * Utilizza le viste di tipo VE , VL e VF.
 * Estende la classe Titolo, dalla quale eredita diversi metodi di utilità e la possibilità
 * di valorizzare i filtri sul titolo cercato.
 * </p>
 * @author
 * @author
 *
 * @version 16-apr-03
 */
public class TitoloLuoCerca extends Titolo {

	private static final long serialVersionUID = -2330308917555009118L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloAutore");
	private String ord;
	private CercaTitoloType cercaTitolo;
	private CercaTitolo factoring;

	/** Costruttore, la connessione al db serve per tutti gli accessi al DB */
	public TitoloLuoCerca(CercaTitolo factoring, CercaTitoloType cercaTitolo, String ord) {
		super();
		this.ord = ord;
		this.cercaTitolo = cercaTitolo;
		this.factoring = factoring;
	}

	/**Esegue la ricerca del titolo discriminando quale vista utilizzare.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	public TableDao cercaTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
		if (cercaTitolo.getArrivoLegame() != null) {
			LegameElementoAutType legameElemento = cercaTitolo.getArrivoLegame().getLegameElementoAut();
			if (legameElemento != null) {
				if (legameElemento.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
					return cercaTitoloPerAutore();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
					return cercaTitoloPerLuogo();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
					return cercaTitoloPerSoggetto();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
					return cercaTitoloPerThesauro();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
					return cercaTitoloPerMarca();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
					return cercaTitoloPerClasse();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
					return cercaTitoloPerLegameTitolo();
				} else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
					//[TODO manca l'UM
					//return cercaTitoloPerLegameTitolo();
				}
			} else if (cercaTitolo.getArrivoLegame().getLegameDoc() != null) {
				//cercaPerLegameDoc(cercaTitolo.getArrivoLegame().getLegameDoc());
			}

		} else { //non ho l'arrivo legame, uso solo i filtri su luogo
			return cercaTitoloConFiltriLuogo();
		}
		return null;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_aut
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerLuogo() throws IllegalArgumentException, InvocationTargetException, Exception {
			Vl_titolo_luo tit = new Vl_titolo_luo();
			Vl_titolo_luoResult tavola = new Vl_titolo_luoResult(tit);
			tit.setLID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
			valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());

			factoring.controllaNumeroRecord(conta(tavola, "countPerLuogo"));

			tavola.executeCustom("selectPerLuogo", ord);
			return tavola;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * Cerca per stringaEsatta, stringaLike o Clet.
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * */
	protected TableDao cercaTitoloConFiltriLuogo() throws IllegalArgumentException, InvocationTargetException, Exception {
		StringaCercaType stringaCerca =
			cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getStringaCerca();
		if (stringaCerca != null) {
			if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
				return cercaTitoloPerNomeEsatto();
			else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
				return cercaTitoloPerNomeLike();
			else
				throw new EccezioneSbnDiagnostico(3040);
		} else if (
			cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getTitoloCLET()
				!= null) {
			//Se arrivo qui è da solo, altrimenti è da utilizzarsi in combinazione
			//con altri elementi
			return cercaTitoloPerClet(
				cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getTitoloCLET());
		}
		throw new EccezioneSbnDiagnostico(3001, "Ricerca fallita");
	}

	/** Esegue la ricerca per nome del titolo con confronto di tipo like
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerClet(String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vf_titolo_luo tit = new Vf_titolo_luo();
		Vf_titolo_luoResult tavola = new Vf_titolo_luoResult(tit);

		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		tit.settaParametro(TableDao.XXXstringaClet, clet);
		factoring.controllaNumeroRecord(conta(tavola, "countPerClet"));

		tavola.executeCustom("selectPerClet", ord);
		return tavola;

	}

	/** Esegue la ricerca per nome del titolo con confronto di tipo like
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerNomeLike() throws IllegalArgumentException, InvocationTargetException, Exception {
		Vf_titolo_luo tit = new Vf_titolo_luo();
		Vf_titolo_luoResult tavola = new Vf_titolo_luoResult(tit);

		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

		tavola.executeCustom("selectPerNomeLike", ord);
		return tavola;

	}

	/** Esegue la ricerca per nome del titolo con confronto di tipo esatto
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerNomeEsatto() throws IllegalArgumentException, InvocationTargetException, Exception {
		Vf_titolo_luo tit = new Vf_titolo_luo();
		Vf_titolo_luoResult tavola = new Vf_titolo_luoResult(tit);

		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

		tavola.executeCustom("selectPerNomeEsatto", ord);
		return tavola;

	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_luo
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ve_titolo_aut_luo tit = new Ve_titolo_aut_luo();
		Ve_titolo_aut_luoResult tavola = new Ve_titolo_aut_luoResult(tit);

		tit.setVID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

		tavola.executeCustom("selectPerAutore", ord);
		return tavola;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_luo
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerLegameTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ve_titolo_tit_c_luo tit = new Ve_titolo_tit_c_luo();
		Ve_titolo_tit_c_luoResult tavola = new Ve_titolo_tit_c_luoResult(tit);

		tit.setBID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		// Inizio modifica almaviva2 28.09.2010 bug mantis 3749
		// viene riportata la stessa modifica fatta nel corrispondente oggetto di Indice countPerTitolo diventa countPerTitolo_Coll
		// mentre selectPerTitolo diventa selectPerTitolo_COll
//		factoring.controllaNumeroRecord(conta(tavola, "countPerTitolo"));
//		tavola.executeCustom("selectPerTitolo", ord);

		factoring.controllaNumeroRecord(conta(tavola, "countPerTitolo_Coll"));
		tavola.executeCustom("selectPerTitolo_Coll", ord);



		// Fine modifica almaviva2 28.09.2010 bug mantis 3749

		return tavola;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_luo
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerClasse() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ve_titolo_cla_luo tit = new Ve_titolo_cla_luo();
		Ve_titolo_cla_luoResult tavola = new Ve_titolo_cla_luoResult(tit);
		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		String chiave = cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo();
		if (chiave.length() < 3)
			throw new EccezioneSbnDiagnostico(3071, "Identificativo classe non corretto");
		tit.setCD_SISTEMA(chiave.substring(0, 3));
		tit.setCD_EDIZIONE(chiave.substring(1, 3));
        //tit.setCD_EDIZIONE(Decodificatore.getCd_tabella("Tb_classe","cd_edizione",chiave.substring(1, 3)));
        tit.setCLASSE(chiave.substring(3));

		factoring.controllaNumeroRecord(conta(tavola, "countPerClasse"));

		tavola.executeCustom("selectPerClasse", ord);
		return tavola;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_luo
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerSoggetto() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ve_titolo_sog_luo tit = new Ve_titolo_sog_luo();
		Ve_titolo_sog_luoResult tavola = new Ve_titolo_sog_luoResult(tit);
		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		tit.setCID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
		factoring.controllaNumeroRecord(conta(tavola, "countPerSoggetto"));
		tavola.executeCustom("selectPerSoggetto", ord);
		return tavola;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_luo
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerThesauro() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ve_titolo_the_luo tit = new Ve_titolo_the_luo();
		Ve_titolo_the_luoResult tavola = new Ve_titolo_the_luoResult(tit);
		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		tit.setDID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
		factoring.controllaNumeroRecord(conta(tavola, "countPerThesauro"));
		tavola.executeCustom("selectPerThesauro", ord);
		return tavola;
	}

	/** Esegue la conta dei record che vengono trovati: ritorna una tavola
	 * contenente un elenco di oggetti vista vl_titolo_luo
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException */
	protected TableDao cercaTitoloPerMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
		Ve_titolo_mar_luo tit = new Ve_titolo_mar_luo();

		Ve_titolo_mar_luoResult tavola = new Ve_titolo_mar_luoResult(tit);
		valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
		tit.setMID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());

		factoring.controllaNumeroRecord(conta(tavola, "countPerMarca"));

		tavola.executeCustom("selectPerMarca", ord);
		return tavola;
	}

	protected void valorizzaFiltri(Vf_titolo_luo luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}

	protected void valorizzaFiltri(Ve_titolo_aut_luo luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVid_f(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}

	protected void valorizzaFiltri(Ve_titolo_luo_aut luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVID(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}
	protected void valorizzaFiltri(Ve_titolo_mar_aut luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVID(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}

	protected void valorizzaFiltri(Ve_titolo_sog_aut luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVID(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}

	protected void valorizzaFiltri(Ve_titolo_cla_aut luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVID(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}

	protected void valorizzaFiltri(Ve_titolo_tit_c_aut luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVID(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}


	// Inizio modifica almaviva2 28.09.2010 bug mantis 3749
	// viene riportata la stessa modifica fatta nel corrispondente oggetto di Indice countPerTitolo diventa countPerTitolo_Coll
	protected void valorizzaFiltri(Ve_titolo_tit_c_luo luogo, CercaDatiTitType cerca)
		throws EccezioneSbnDiagnostico {
		super.valorizzaFiltri(luogo, cerca);
		ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
		if (elAut != null) {
			CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
			if (canali != null) {
				//luogo.setVid(canali.getT001());
				StringaCercaType stringaCerca = canali.getStringaCerca();
				if (stringaCerca != null) {
					if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
						luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
					if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
						luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
				}
			}
		}
	}

	protected void valorizzaFiltri(Ve_titolo_mar_luo luogo, CercaDatiTitType cerca)
	throws EccezioneSbnDiagnostico {
	super.valorizzaFiltri(luogo, cerca);
	ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
	if (elAut != null) {
		CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
		if (canali != null) {
			//luogo.setVid_f(canali.getT001());
			StringaCercaType stringaCerca = canali.getStringaCerca();
			if (stringaCerca != null) {
				if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
					luogo.settaParametro(TableDao.XXXStringaEsattaLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
				if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
					luogo.settaParametro(TableDao.XXXStringaLikeLuogo, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
			}
		}
	}
}



	// Fine modifica almaviva2 28.09.2010 bug mantis 3749





}
