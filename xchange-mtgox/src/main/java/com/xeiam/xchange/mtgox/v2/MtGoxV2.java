/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.mtgox.v2;

import java.math.BigDecimal;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import si.mazi.rescu.ParamsDigest;

import com.xeiam.xchange.mtgox.v2.dto.account.MtGoxAccountInfo;
import com.xeiam.xchange.mtgox.v2.dto.account.MtGoxBitcoinDepositAddress;
import com.xeiam.xchange.mtgox.v2.dto.account.MtGoxWithdrawalResponse;
import com.xeiam.xchange.mtgox.v2.dto.marketdata.MtGoxDepth;
import com.xeiam.xchange.mtgox.v2.dto.marketdata.MtGoxTicker;
import com.xeiam.xchange.mtgox.v2.dto.marketdata.MtGoxTrade;
import com.xeiam.xchange.mtgox.v2.dto.trade.polling.MtGoxGenericResponse;
import com.xeiam.xchange.mtgox.v2.dto.trade.polling.MtGoxLag;
import com.xeiam.xchange.mtgox.v2.dto.trade.polling.MtGoxOpenOrder;

/**
 * @author timmolter
 */
@Path("api/2")
public interface MtGoxV2 {

  @GET
  @Path("money/order/lag?raw")
  MtGoxLag getLag();

  @GET
  @Path("{ident}{currency}/money/ticker?raw")
  MtGoxTicker getTicker(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency);

  @GET
  @Path("{ident}{currency}/money/depth/fetch?raw")
  MtGoxDepth getDepth(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency);

  @GET
  @Path("{ident}{currency}/money/depth/full?raw")
  MtGoxDepth getFullDepth(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency);

  @GET
  @Path("{ident}{currency}/money/trades/fetch?raw")
  MtGoxTrade[] getTrades(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency);

  @GET
  @Path("{ident}{currency}/money/trades/fetch?raw")
  MtGoxTrade[] getTrades(@PathParam("ident") String tradeableIdentifier, @PathParam("currency") String currency, @QueryParam("raw") String raw, @QueryParam("since") long since);

  @POST
  @Path("money/info?raw")
  MtGoxAccountInfo getAccountInfo(@HeaderParam("Rest-Key") String apiKey, @HeaderParam("Rest-Sign") ParamsDigest postBodySignatureCreator, @FormParam("nonce") long nonce);

  @POST
  @Path("money/bitcoin/address?raw")
  MtGoxBitcoinDepositAddress requestDepositAddress(@HeaderParam("Rest-Key") String apiKey, @HeaderParam("Rest-Sign") ParamsDigest postBodySignatureCreator, @FormParam("nonce") long nonce,
      @FormParam("description") String description, @FormParam("ipn") String notificationUrl);

  @POST
  @Path("money/orders?raw")
  MtGoxOpenOrder[] getOpenOrders(@HeaderParam("Rest-Key") String apiKey, @HeaderParam("Rest-Sign") ParamsDigest postBodySignatureCreator, @FormParam("nonce") long nonce);

  @POST
  @Path("money/bitcoin/send_simple?raw")
  MtGoxWithdrawalResponse withdrawBtc(@HeaderParam("Rest-Key") String apiKey, @HeaderParam("Rest-Sign") ParamsDigest postBodySignatureCreator, @FormParam("nonce") long nonce,
      @FormParam("address") String address, @FormParam("amount_int") int amount, @FormParam("fee_int") int fee, @FormParam("no_instant") boolean noInstant, @FormParam("green") boolean green);

  /**
   * @param postBodySignatureCreator
   * @param amount can be omitted to place market order
   */
  @POST
  @Path("{tradeIdent}{currency}/private/order/add")
  MtGoxGenericResponse placeOrder(@HeaderParam("Rest-Key") String apiKey, @HeaderParam("Rest-Sign") ParamsDigest postBodySignatureCreator, @FormParam("nonce") long nonce,
      @PathParam("tradeIdent") String tradableIdentifier, @PathParam("currency") String currency, @FormParam("type") String type, @FormParam("amount_int") BigDecimal amount,
      @FormParam("price_int") String price);

  /**
   * Note: I know it's weird to have BTCEUR hardcoded in the URL, but it really doesn't seems to matter. BTCUSD works too.
   * <p>
   * 
   * @param apiKey
   * @param postBodySignatureCreator
   * @param nonce
   * @param orderId
   * @return
   */
  @POST
  @Path("BTCEUR/private/order/cancel")
  MtGoxGenericResponse cancelOrder(@HeaderParam("Rest-Key") String apiKey, @HeaderParam("Rest-Sign") ParamsDigest postBodySignatureCreator, @FormParam("nonce") long nonce,
      @FormParam("oid") String orderId);

}
