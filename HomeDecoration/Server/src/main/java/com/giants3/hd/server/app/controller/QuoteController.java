package com.giants3.hd.server.app.controller;


import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.QuotationService;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 报价
 */
@Controller
@RequestMapping("/quote")
public class QuoteController extends BaseController {



}
