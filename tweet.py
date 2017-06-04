from summary import FrequencySummarizer
import urllib.request,re , html,sys,getopt
summarize= FrequencySummarizer()
def main(argv):
   username = ''
   date = ''
   try:
      opts, args = getopt.getopt(argv,"hu:d:",["username=","date="])
   except getopt.GetoptError:
      print("test.py -u <inputfile> -d <outputfile>")
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print("test.py -u <inputfile> -d <outputfile>")
         sys.exit()
      elif opt in ("-u", "--username"):
         username = arg
      elif opt in ("-d", "--date"):
         date = arg
   return username,date
def countDot(word):
    dot=word.count(".")
    if dot >= 6:
        return 2
    return 1
user=""
prev=""
if __name__ == "__main__":
   user,prev=main(sys.argv[1:])

if user=="" or prev=="":
    user="sbyudhoyono"
    prev=" 12 31"
url = "https://tweets-grabber.000webhostapp.com/grab.php?username="+user
x = urllib.request.urlopen(url)
raw_data = x.read()
twit=str(raw_data).split("<br>")
tweets=[]
for tw in twit[2:]:
    tw=re.sub(r'\\x[a-zA-Z0-9]{2}','',tw)
    if "followe" in tw:
        continue
    tw=tw.replace("\\n"," ")
    rawa=tw.split("||")
    if rawa[0]==prev:
        tweets.append(rawa[1])
dummy4=". ".join(tweets)
dummy4=re.sub(r'\.+','.',dummy4)
dummy4=html.unescape(dummy4)
print(dummy4)
summ=summarize.summarize(dummy4,countDot(dummy4))
print(summ)
