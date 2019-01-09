package MDS
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import java.nio.file.Paths

class MDSWebScenario extends Simulation  {
  val baseMdsUrl = "**************"
  val baseStsUrl = "**************"

  val webHttpProtocol = http
    //.inferHtmlResources(BlackList(""".*\.js.*""", """.*\.css.*""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png""", """.*/css/shared""", """.*/js""", """.*\.css"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
    .disableFollowRedirect

  val headers_1 = Map("Origin" -> baseStsUrl)

  val webScn = scenario("Authorize on MDS Web")
    .exec(
      http("sts_login_get")
        .get(baseStsUrl + "/Account/LogOn?ReturnUrl=***********************")
        .check(
          css("input[name='__RequestVerificationToken']", "value")
            .saveAs("requestVerificationToken")
        )
    )
    .exec(
      http("sts_login_post")
        .post(baseStsUrl + "/Account/LogOn?returnUrl=************************")
        .headers(headers_1)
        .formParam("Email", "******")
        .formParam("Password", "*****")
        .formParam("__RequestVerificationToken", "${requestVerificationToken}")
        .check(status.is(302))
        .check(
          header("Location").saveAs("mdsLocation")
        )
    )
    .exec(
      http("mds_web_get")
        .get("${mdsLocation}")
        .check(
          css("#touchpoint-respondent-picker-container")
            .exists
        )
    )

  setUp(
    webScn.inject(
      rampConcurrentUsers(3) to (5) during (30 seconds)
    ).protocols(webHttpProtocol)
  )
}
