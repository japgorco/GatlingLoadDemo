package MDS

class Parameters {
   def baseUrl(): String =
     if (System.getenv("MDS_API_URL") != null)
      System.getenv("MDS_API_URL")
     else
       "https://********.com"
  def accessId(): String =
    if (System.getenv("MDS_API_ACCESS_ID") != null)
      System.getenv("MDS_API_ACCESS_ID")
    else
      "**********"
  def applicationId(): String =
    if (System.getenv("MDS_API_APPLICATION_ID") != null)
      System.getenv("MDS_API_APPLICATION_ID")
    else
      "*******"
}
