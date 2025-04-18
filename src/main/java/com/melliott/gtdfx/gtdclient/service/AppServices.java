package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.Config;
import com.melliott.gtdfx.gtdclient.ConfigLoader;

public class AppServices {
    public final AlertService alertService = new AlertService();
    public final JsonService jsonService = new JsonService();
    public final HttpService httpService = new HttpService();
    public final Config config = new ConfigLoader(jsonService).getConfig();
    public final StatusService statusService = new StatusService();
}
