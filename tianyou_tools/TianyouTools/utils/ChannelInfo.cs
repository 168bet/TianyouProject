using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;

namespace TianyouMultiChannel
{
    [DataContract]
    class ChannelInfo
    {
        [DataMember(Order = 1, Name = "result")]
        public Result result { get; set; }

        [DataContract]
        public class Result
        {
            [DataMember(Order = 1, Name = "code")]
            public int code { get; set; }

            [DataMember(Order = 2, Name = "msg")]
            public string msg { get; set; }

            [DataMember(Order = 3, Name = "productinfo")]
            public Productinfo[] productinfo { get; set; }

            [DataContract]
            public class Productinfo
            {
                [DataMember(Order = 1, Name = "id")]
                public string id { get; set; }

                [DataMember(Order = 2, Name = "name")]
                public string name { get; set; }

                [DataMember(Order = 3, Name = "channelid")]
                public string channelid { get; set; }
            }
        }
    }
}
