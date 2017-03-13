using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;

namespace TianyouMultiChannel
{
    [DataContract]
    class PayParam
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

            [DataMember(Order = 3, Name = "payinfo")]
            public Payinfo[] payinfo { get; set; }

            [DataContract]
            public class Payinfo
            {
                [DataMember(Order = 1, Name = "id")]
                public string id { get; set; }

                [DataMember(Order = 2, Name = "money")]
                public string money { get; set; }

                [DataMember(Order = 3, Name = "product_id")]
                public string product_id { get; set; }

                [DataMember(Order = 4, Name = "product_name")]
                public string product_name { get; set; }
            }
        }
    }
}
