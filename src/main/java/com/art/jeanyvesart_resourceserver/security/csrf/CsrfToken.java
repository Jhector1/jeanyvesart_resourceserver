package com.art.jeanyvesart_resourceserver.security.csrf;

import java.io.Serializable;

public interface CsrfToken extends Serializable {
 
  String getHeaderName();
  String getParameterName();
  String getToken();
}