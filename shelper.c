#include "array.h"
#include <stdlib.h>
#ifdef DMALLOC
#include "dmalloc.h"
#endif
int getInt(char* s,char token, char** next){
	char v[16];
	char c = *s;
	int i = 0;
	int b = 0;
	*next = 0;
	while( c != '\0'){
		if(c==token){
			*next=s+1;
			v[i]='\0';
			b=1;
			break;
		}
		v[i]=c;
		i++;
		s++;
		c=*s;
	}
	if(b==1) return atoi(v);
	return -1;
}

int parse_http_header(char* data,array *headers,int *content_length){
	char* src = data;
    char *uri = NULL, *proto = NULL, *method = NULL, con_length_set;
    int is_key = 1, key_len = 0, is_ws_after_key = 0, in_folding;
    char *value = NULL, *key = NULL;
    char *reqline_host = NULL;
    int reqline_hostlen = 0;
    int line = 0;

    int request_line_stage = 0;
    size_t i, first;

    int done = 0;

    /*
     * Request: "^(GET|POST|HEAD) ([^ ]+(\\?[^ ]+|)) (HTTP/1\\.[01])$"
     * Option : "^([-a-zA-Z]+): (.+)$"
     * End    : "^$"
     */
	
        /* we are in keep-alive and might get \r\n after a previous POST request.*/
	if(src[0]=='\r' && src[1]=='\n'){
		src+=2;
	}
        first=0;
	for(i=0;src[i]!='\0';i++){
		char* cur = src+i;
        if (is_key)
        {
            size_t j;
            int got_colon = 0;

            /**
             * 1*<any CHAR except CTLs or separators>
             * CTLs == 0-31 + 127
             *
             */
            switch(*cur)
            {
            case ':':
                is_key = 0;

                value = cur + 1;

                if (is_ws_after_key == 0)
                {
                    key_len = i - first;
                }
                is_ws_after_key = 0;

                break;
            case '(':
            case ')':
            case '<':
            case '>':
            case '@':
            case ',':
            case ';':
            case '\\':
            case '\"':
            case '/':
            case '[':
            case ']':
            case '?':
            case '=':
            case '{':
            case '}':
                return -1;
            case ' ':
            case '\t':
                if (i == first)
                {
                    is_key = 0;
                    in_folding = 1;
                    value = cur;

                    break;
                }


                key_len = i - first;

                /* skip every thing up to the : */
                for (j = 1; !got_colon; j++)
                {
                    switch(src[j + i])
                    {
                    case ' ':
                    case '\t':
                        /* skip WS */
                        continue;
                    case ':':
                        /* ok, done */

                        i += j - 1;
                        got_colon = 1;

                        break;
                    default:
                        /* error */
			return -1;
                    }
                }

                break;
            case '\r':
                if (src[i+1] == '\n' && i == first)
                {
                    /* End of Header */
                    src[i] = '\0';
                    src[i+1] = '\0';

                    i++;

                    done = 1;

                    break;
                }
                else
                {
                    return -1;
                }
                /* fall thru */
            case 0: /* illegal characters (faster than a if () :) */
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 127:
                return -1;
            default:
                /* ok */
                break;
            }
        }
        else
        {
            switch(*cur)
            {
            case '\r':
                if (src[i+1] == '\n')
                {
                    data_string *ds = NULL;

                    /* End of Headerline */
                    src[i] = '\0';
                    src[i+1] = '\0';

                    //put all the key:value into
                    {
                        int s_len;
                        key = src + first;

                        s_len = cur - value;

                        /* strip trailing white-spaces */
                        for (; s_len > 0 &&
                                (value[s_len - 1] == ' ' ||
                                 value[s_len - 1] == '\t'); s_len--);

                        value[s_len] = '\0';

                        if (s_len > 0)
                        {
                            int cmp = 0;
                            int bnew = 0;
                            if (NULL == (ds = (data_string *)array_get_unused_element(headers, TYPE_STRING)))
                            {
                                //sureone: allocate a new data string
                                ds = data_string_init();
                                bnew = 1;
                            }
                            buffer_copy_string_len(ds->key, key, key_len);
                            buffer_copy_string_len(ds->value, value, s_len);
                            //sureone add the key-value mapping into the header
                            array_insert_unique(headers, (data_unset *)ds);
				log_error_write(__FILE__,__LINE__,"ss",ds->key->ptr,ds->value->ptr);

                        }
                        else
                        {
                            /* empty header-fields are not allowed by HTTP-RFC, we just ignore them */
                        }
                    }

                    i++;
                    first = i+1;
                    is_key = 1;
                    value = 0;
                }
                else
                {
                    return -1;
                }
                break;
            case ' ':
            case '\t':
                /* strip leading WS */
                if (value == cur) value = cur+1;
            default:
                if (*cur >= 0 && *cur < 32 && *cur != '\t')
                {
                    return -1;
                }
                break;
            }
        }
    }

    {
        data_string *ds;
        ds = (data_string *)array_get_element(headers,"request");
        if ( NULL == ds)
        {
            return 0;
        }
        if (strncmp(ds->value->ptr,"post",4)==0)
        {
            ds = (data_string *)array_get_element(headers,"content-length");
            if ( NULL == ds)
            {
                return -1;
            }
            *content_length=atoi(ds->value->ptr);
            if (*content_length > 4*1024)
            {
                return -1;
            }
            else
            {
                return 1;
	    }
	}
	}
	return 0;

		
}
