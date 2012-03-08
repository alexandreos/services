/*
 * EncomendaZ
 * 
 * Copyright (c) 2011, EncomendaZ <http://encomendaz.net>.
 * All rights reserved.
 * 
 * EncomendaZ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://gnu.org/licenses>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.encomendaz.rest.tracking;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.encomendaz.rest.DateSerializer;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Tracking {

	private Date data;

	private String local;

	private String situacao;

	private String observacao;

	public static Tracking parse(RegistroRastreamento registro) {
		if (registro == null) {
			throw new IllegalArgumentException("O registro de rastreamento não pode ser nulo.");
		}

		Tracking response = new Tracking();
		response.data = registro.getDataHora();
		response.local = parse(registro.getLocal());
		response.situacao = registro.getAcao();
		response.observacao = parse(registro.getDetalhe());

		return response;
	}

	private static String parse(String local) {
		Pattern pattern = Pattern.compile("^(.*?[A-Z]{2,3}) (.*) - (.*)/(\\w{2})$");
		Matcher matcher = pattern.matcher(local == null ? "" : local);
		String resultado = local;

		if (matcher.matches()) {
			String p1 = matcher.group(1);
			String p2;
			String p3 = matcher.group(4);

			if (matcher.group(2).equals(matcher.group(3))) {
				p2 = matcher.group(2);
			} else {
				p2 = matcher.group(2) + " – " + matcher.group(3);
			}

			resultado = p1 + " " + parseToFirstUpper(p2) + "/" + p3;
		}

		return resultado;
	}

	private static String parseToFirstUpper(String texto) {
		StringBuffer buffer = new StringBuffer();

		for (String palavra : texto.split(" ")) {
			if (palavra.length() > 2) {
				buffer.append(palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase());
			} else {
				buffer.append(palavra.toLowerCase());
			}

			buffer.append(" ");
		}

		return buffer.toString().trim();
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String result;

		try {
			result = mapper.writeValueAsString(this);

		} catch (IOException e) {
			result = super.toString();
		}

		return result;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@JsonSerialize(include = NON_NULL)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}