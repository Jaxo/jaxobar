var urlRegexp = new RegExp(
   "(?:" +
      "[a-z][\\w-]+:(?:\\/{1,3}|[a-z0-9%])" +
      "|" +
      "www\\d{0,3}[.]" +
      "|" +
      "[a-z0-9.\\-]+[.][a-z]{2,4}\\/" +
   ")" +
   "(?:" +
      "[^\\s()<>]+" +
      "|" +
      "\\((?:[^\\s()<>]+|(?:\\(?:[^\\s()<>]+\\)))*\\)" +
   ")+" +
   "(?:" +
      "\\((?:[^\\s()<>]+|(?:\\(?:[^\\s()<>]+\\)))*\\)" +
      "|" +
      "[^\\s`!()\\[\\]{};:'\".,<>?«»\u201c\u201d\u2018\u2019]" +
   ")",
   "mig"
);

function extractUrls(text) {
   var urls = [];
   while ((res = urlRegexp.exec(text)) !== null) {
      urls.push(parseURL(res[0]));
   }
   return urls;
}

function urlize(input, elt) {
   var res;
   var start = 0;
   urlRegexp.lastIndex = 0;
   while ((res = urlRegexp.exec(input)) !== null) {
      if (start < res.index) {
         elt.appendChild(document.createTextNode(input.substring(start, res.index)));
      }
      var anchorElt = document.createElement("A");
      anchorElt.setAttribute("href", res[0]);
      anchorElt.setAttribute("target", "_blank");
      anchorElt.textContent = res[0];
      elt.appendChild(anchorElt);
      start = urlRegexp.lastIndex;
   }
   if (start < input.length) {
      elt.appendChild(document.createTextNode(input.substring(start, input.length)));
   }
}

function parseURL(url) {
   // var url = parseURL('http://abc.com:8080/dir/index.html?id=255&m=hello#top');
   // url.file;     // = 'index.html'
   // url.hash;     // = 'top'
   // url.host;     // = 'abc.com'
   // url.query;    // = '?id=255&m=hello'
   // url.params;   // = Object = { id: 255, m: hello }
   // url.path;     // = '/dir/index.html'
   // url.segments; // = Array = ['dir', 'index.html']
   // url.port;     // = '8080'
   // url.protocol; // = 'http'
   // url.source;   // = 'http://abc.com:8080/dir/index.html?id=255&m=hello#top'
   var a =  document.createElement('A');
   a.href = url;
   return {
      source: url,
      protocol: a.protocol.replace(':',''),
      host: a.hostname,
      port: a.port,
      query: a.search,
      params: (
         function() {
            var ret = {};
            var seg = a.search.replace(/^\?/,'').split('&');
            var len = seg.length;
            var s;
            for (var i=0; i<len; ++i) {
               if (!seg[i]) { continue; }
               s = seg[i].split('=');
               ret[s[0]] = s[1];
            }
            return ret;
         }
      )(),
      file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
      hash: a.hash.replace('#',''),
      path: a.pathname.replace(/^([^\/])/,'/$1'),
      relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
      segments: a.pathname.replace(/^\//,'').split('/')
   };
}
