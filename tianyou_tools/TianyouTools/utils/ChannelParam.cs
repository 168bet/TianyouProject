using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;

namespace TianyouMultiChannel
{
    [DataContract]
    class ChannelParam
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

            [DataMember(Order = 3, Name = "channelinfo")]
            public Channelinfo channelinfo { get; set; }

            [DataContract]
            public class Channelinfo
            {
                [DataMember(Order = 1, Name = "id")]
                public string id { get; set; }

                [DataMember(Order = 2, Name = "name")]
                public string name { get; set; }

                [DataMember(Order = 3, Name = "game_id")]
                public string game_id { get; set; }

                [DataMember(Order = 4, Name = "channel_id")]
                public string channel_id { get; set; }

                [DataMember(Order = 5, Name = "appid")]
                public string appid { get; set; }

                [DataMember(Order = 6, Name = "package")]
                public string package { get; set; }

                [DataMember(Order = 7, Name = "appkey")]
                public string appkey { get; set; }

                [DataMember(Order = 8, Name = "appsecret")]
                public string appsecret { get; set; }

                [DataMember(Order = 9, Name = "create_time")]
                public string create_time { get; set; }

                [DataMember(Order = 10, Name = "update_time")]
                public string update_time { get; set; }

                [DataMember(Order = 11, Name = "channel_type")]
                public string channel_type { get; set; }
            }
        }
    }
}
