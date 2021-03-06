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
package it.iccu.sbn.ejb.vo.validation;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class JSScriptVO extends SerializableVO {

	private static final long serialVersionUID = -4246651971271596010L;

	private String key;
	private String script;
	private String descr;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = trimAndSet(key);
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = trimAndSet(script);
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = trimAndSet(descr);
	}

}
