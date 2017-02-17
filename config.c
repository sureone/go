#include "base.h"
#define PATCH(x) con->conf.x = s->x
int config_setup_connection(server *srv, connection *con) {
	specific_config *s = srv->config_storage[0];

	PATCH(global_kbytes_per_second);
	PATCH(global_bytes_per_second_cnt);

	con->keep_alive=1;
	con->conf.global_bytes_per_second_cnt_ptr = &s->global_bytes_per_second_cnt;

	PATCH(is_ssl);

	PATCH(ssl_pemfile);
#ifdef USE_OPENSSL
	PATCH(ssl_ctx);
#endif
	PATCH(ssl_ca_file);
	PATCH(ssl_cipher_list);
	PATCH(ssl_dh_file);
	PATCH(ssl_ec_curve);
	PATCH(ssl_honor_cipher_order);
	PATCH(ssl_use_sslv2);
	PATCH(ssl_use_sslv3);

	PATCH(ssl_verifyclient);
	PATCH(ssl_verifyclient_enforce);
	PATCH(ssl_verifyclient_depth);
	PATCH(ssl_verifyclient_username);
	PATCH(ssl_verifyclient_export_cert);
	PATCH(ssl_disable_client_renegotiation);

	return 0;
}
