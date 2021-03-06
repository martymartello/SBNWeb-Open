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
//	SBNWeb - Rifacimento ClientServer
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;


public class EsaminaTitoliConFiltroForm extends InterrogazioneTitoloForm {


	private static final long serialVersionUID = 5654750608190039624L;
	private String bidTitRifer;
	private String descBidTitRifer;

	public String getBidTitRifer() {
		return bidTitRifer;
	}
	public void setBidTitRifer(String bidTitRifer) {
		this.bidTitRifer = bidTitRifer;
	}
	public String getDescBidTitRifer() {
		return descBidTitRifer;
	}
	public void setDescBidTitRifer(String descBidTitRifer) {
		this.descBidTitRifer = descBidTitRifer;
	}

}
