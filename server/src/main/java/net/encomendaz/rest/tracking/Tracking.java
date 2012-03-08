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

import net.encomendaz.rest.DateSerializer;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "date", "city", "state", "country", "description", "status" })
public class Tracking {

	private Parser parser;

	private Tracking(Parser parser) {
		this.parser = parser;
	}

	public static Tracking parse(RegistroRastreamento registro) {
		if (registro == null) {
			throw new IllegalArgumentException("O registro de rastreamento não pode ser nulo.");
		}

		Parser parser = new CorreiosParser(registro);
		Tracking tracking = new Tracking(parser);

		return tracking;
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

	public String getStatus() {
		return parser.getStatus();
	}

	@JsonSerialize(include = NON_NULL)
	public String getDescription() {
		return parser.getDescription();
	}

	public String getCity() {
		return parser.getCity();
	}

	public String getState() {
		return parser.getState();
	}

	public String getCountry() {
		return parser.getCountry();
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getDate() {
		return parser.getDate();
	}
}
