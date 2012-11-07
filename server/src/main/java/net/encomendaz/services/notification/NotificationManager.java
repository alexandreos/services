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
package net.encomendaz.services.notification;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.encomendaz.services.monitoring.Monitoring;
import net.encomendaz.services.monitoring.MonitoringManager;
import net.encomendaz.services.tracking.Trace;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.util.Strings;

import org.jboss.resteasy.client.ProxyFactory;

public class NotificationManager {

	public static void register(String deviceToken, String clientId) {
		Registration registration = new Registration();
		registration.setAlias(clientId);

		RegistrationProxy proxy = createProxy(RegistrationProxy.class);
		proxy.register(deviceToken, registration);
	}

	public static void send(Monitoring monitoring, Tracking tracking) throws Exception {
		if (monitoring.getClientId().indexOf("@") > 0) {
			mail(monitoring, tracking);

		} else {
			push(monitoring, tracking);
		}
	}

	private static void mail(Monitoring monitoring, Tracking tracking) throws AddressException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "O status da encomenda " + monitoring.getTrackId() + " mudou!";

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("cleverson.sacramento@gmail.com"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(monitoring.getClientId()));
		msg.setSubject(msgBody);
		msg.setText(msgBody);
		Transport.send(msg);
	}

	private static void push(Monitoring monitoring, Tracking tracking) {
		Aps aps = new Aps();
		aps.setAlert(buildMessage(monitoring, tracking));
		aps.setSound("default");
		aps.setBadge(MonitoringManager.countUnread(monitoring.getClientId()));

		Push push = new Push();
		push.addAlias(monitoring.getClientId());
		push.setTrackId(monitoring.getTrackId());
		push.setAps(aps);

		NotificationProxy proxy = createProxy(NotificationProxy.class);
		proxy.notify(push);
	}

	private static <T> T createProxy(Class<T> proxyClass) {
		return ProxyFactory.create(proxyClass, "https://go.urbanairship.com", new AirshipClientExecutor());
	}

	private static String buildMessage(Monitoring monitoring, Tracking tracking) {
		StringBuffer message = new StringBuffer();

		message.append("A encomenda ");
		message.append(monitoring.getTrackId());

		if (!Strings.isEmpty(monitoring.getLabel())) {
			message.append(" (" + monitoring.getLabel() + ")");
		}

		message.append(" ");

		switch (tracking.getLastTrace().getStatus()) {
			case ACCEPTANCE:
				message.append("foi postada");
				message.append(buildCity(tracking.getLastTrace()));

				break;
			case ENROUTE:
				message.append("foi encaminhada para");
				message.append(buildNextStop(tracking.getLastTrace()));

				break;
			case CHECKED:
				message.append("foi verificada");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			case DELIVERING:
				message.append("saiu para entrega");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			case DELIVERED:
				message.append("foi entregue");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			case AWAITING:
				message.append("está aguardando retirada");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			default:
				message.append("está com um status desconhecido");
		}

		message.append(".");

		return message.toString();
	}

	private static String buildCity(Trace trace) {
		StringBuffer city = new StringBuffer();

		if (!Strings.isEmpty(trace.getCity())) {
			city.append(" em " + trace.getCity());

			if (!Strings.isEmpty(trace.getState())) {
				city.append("/" + trace.getState());
			}
		}

		return city.toString();
	}

	private static String buildNextStop(Trace trace) {
		String nextStop = " o próximo destino";

		// Em trânsito para
		// Encaminhado para

		return nextStop;
	}
}
