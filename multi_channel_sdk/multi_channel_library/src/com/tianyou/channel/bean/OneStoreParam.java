package com.tianyou.channel.bean;

import java.util.List;

public class OneStoreParam {

	/**
     * api_version : 4
     * identifier : 1484131641772
     * method : purchase
     * result : {"code":"0000","message":"Succeed in request","count":1,"txid":"TX_00000000423399","receipt":"MIIIFQYJKoZIhvcNAQcCoIIIBjCCCAICAQExDzANBglghkgBZQMEAgEFADBzBgkqhkiG9w0BBwGgZgRkMjAxNzAxMTExOTQ3MjR8VFhfMDAwMDAwMDA0MjMzOTl8MDEwMTIzNDU2Nzh8T0EwMDcwOTgwMHwwOTEwMDY3MTAxfDMzMDB8MjAxNzAxMTExODQ3MjA1NDg0Nnx8MTY1sd3IraCCBe8wggXrMIIE06ADAgECAgQBFBdPMA0GCSqGSIb3DQEBCwUAME8xCzAJBgNVBAYTAktSMRIwEAYDVQQKDAlDcm9zc0NlcnQxFTATBgNVBAsMDEFjY3JlZGl0ZWRDQTEVMBMGA1UEAwwMQ3Jvc3NDZXJ0Q0EyMB4XDTE2MTIxNDAxMDgwMFoXDTE3MTIyMTE0NTk1OVowgYwxCzAJBgNVBAYTAktSMRIwEAYDVQQKDAlDcm9zc0NlcnQxFTATBgNVBAsMDEFjY3JlZGl0ZWRDQTEbMBkGA1UECwwS7ZWc6rWt7KCE7J6Q7J247KadMQ8wDQYDVQQLDAbshJzrsoQxJDAiBgNVBAMMG+yXkOyKpOy8gOydtCDtlIzrnpjri5so7KO8KTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMvxgBh37MP2f5CnALAY1z8/Vb+JR52Sjic2bXFI/B3NYUgDk6q707t0uHA4WslZMGQSq0unqx880o8NXpUyA7XzO//TGNV8yNYJ5rBK/YGm7FdCBLSfV6moLhkj0WmGlpYjAZcwX2wvEKEjpATBw+ClfaxOUdtbIdyxwqFV9NEQjc1R8G4g7aZBd6bT7Zfv6V9Vdjk+i56fdOAIw6PaYoIodsCWsuAX6szSgGkZmX8/m147BkSGLhovWnz6PYeR+RtHwxiTof26sE6ScKCV3qqId+otlO7jUWr8YMQ8NNmDUS+wudNCS7mPCSMo+3xsm6oqdQQIAKYvyus8tIIuI3ECAwEAAaOCAo8wggKLMIGPBgNVHSMEgYcwgYSAFLZ0qZuSPMdRsSKkT7y3PP4iM9d2oWikZjBkMQswCQYDVQQGEwJLUjENMAsGA1UECgwES0lTQTEuMCwGA1UECwwlS29yZWEgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkgQ2VudHJhbDEWMBQGA1UEAwwNS0lTQSBSb290Q0EgNIICEAQwHQYDVR0OBBYEFLx+WC76Agm3/Pty43ggfjyQjWZvMA4GA1UdDwEB/wQEAwIGwDCBgwYDVR0gAQH/BHkwdzB1BgoqgxqMmkQFBAEDMGcwLQYIKwYBBQUHAgEWIWh0dHA6Ly9nY2EuY3Jvc3NjZXJ0LmNvbS9jcHMuaHRtbDA2BggrBgEFBQcCAjAqHii8+AAgx3jJncEcx1gAIMcg1qiuMKwEx0AAIAAxsUQAIMeFssiy5AAuMHoGA1UdEQRzMHGgbwYJKoMajJpECgEBoGIwYAwb7JeQ7Iqk7LyA7J20IO2UjOuemOuLmyjso7wpMEEwPwYKKoMajJpECgEBATAxMAsGCWCGSAFlAwQCAaAiBCARdfi/q+spzK0OUo4ZsARo1VF+YRa7vuyF8SFbeVYstjB+BgNVHR8EdzB1MHOgcaBvhm1sZGFwOi8vZGlyLmNyb3NzY2VydC5jb206Mzg5L2NuPXMxZHA5cDQzOSxvdT1jcmxkcCxvdT1BY2NyZWRpdGVkQ0Esbz1Dcm9zc0NlcnQsYz1LUj9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0MEYGCCsGAQUFBwEBBDowODA2BggrBgEFBQcwAYYqaHR0cDovL29jc3AuY3Jvc3NjZXJ0LmNvbToxNDIwMy9PQ1NQU2VydmVyMA0GCSqGSIb3DQEBCwUAA4IBAQBtV7pMR/Ar4bphc2L+KDQ+8+LkYl5VRdGJHFWFSTU3txgzQnXMidmim0Syu0qX4ki1+t3nttIU8skGNeNtbFovsD92Lu1Ygg8nWRsimz6R8ICrx8vkqBcd5exRiU7L8wU7DQF7Vo1H0gAlKgFlEfmjI7Tt2TabALNiKzftvaMKvEH+/RT7OHEnC4sSPUMUMDpP+qodqnAE7T+W6FOmPv9rNkC+euN5BRpm8SJD5qAI9OUUtQSUneH+SUKeiktz04JSFxEILGq4WIwVVwvGh3bOgutHGzO7OEAlTa9mGhzBQw7r/+M4lUF9XDFOHMSLvpR74cDubLQS0gpMiiNhoBtfMYIBgjCCAX4CAQEwVzBPMQswCQYDVQQGEwJLUjESMBAGA1UECgwJQ3Jvc3NDZXJ0MRUwEwYDVQQLDAxBY2NyZWRpdGVkQ0ExFTATBgNVBAMMDENyb3NzQ2VydENBMgIEARQXTzANBglghkgBZQMEAgEFADANBgkqhkiG9w0BAQsFAASCAQBc1hlgptiBPSaYdhRSg/mZtoPwi5PU4wdV3QNA+b2qXtIgvPPbgPp2yj8IvF5QKGIM+hZ5keKg0dBGKzudqTfLJk1V1kAApzC++tHEClANEZrY5F8l9ZMecqLc17Et0mdtHRiiU/Ek+drheVOWbF1D8arElYtJ1xsybppkuTI+pnNxILFFLBm5nUeuqJ1E3/VmEYhV0GKUfNEZd/Z58NFpavp6q6Ljw3+svkoIt8sC+G2vUIdpw3dDI4FYow072K1BJ28BUB+1jAieu60v3237mD7o1ttDPOHPuHdIto5tkAaiFK9fk3PZwj5OeItPvShQjxj3moUTa3OklwFkNdTG","product":[{"name":"165금화","kind":"consumable","id":"0910067101","price":3300}]}
     */

    private String api_version;
    private String identifier;
    private String method;
    /**
     * code : 0000
     * message : Succeed in request
     * count : 1
     * txid : TX_00000000423399
     * receipt : MIIIFQYJKoZIhvcNAQcCoIIIBjCCCAICAQExDzANBglghkgBZQMEAgEFADBzBgkqhkiG9w0BBwGgZgRkMjAxNzAxMTExOTQ3MjR8VFhfMDAwMDAwMDA0MjMzOTl8MDEwMTIzNDU2Nzh8T0EwMDcwOTgwMHwwOTEwMDY3MTAxfDMzMDB8MjAxNzAxMTExODQ3MjA1NDg0Nnx8MTY1sd3IraCCBe8wggXrMIIE06ADAgECAgQBFBdPMA0GCSqGSIb3DQEBCwUAME8xCzAJBgNVBAYTAktSMRIwEAYDVQQKDAlDcm9zc0NlcnQxFTATBgNVBAsMDEFjY3JlZGl0ZWRDQTEVMBMGA1UEAwwMQ3Jvc3NDZXJ0Q0EyMB4XDTE2MTIxNDAxMDgwMFoXDTE3MTIyMTE0NTk1OVowgYwxCzAJBgNVBAYTAktSMRIwEAYDVQQKDAlDcm9zc0NlcnQxFTATBgNVBAsMDEFjY3JlZGl0ZWRDQTEbMBkGA1UECwwS7ZWc6rWt7KCE7J6Q7J247KadMQ8wDQYDVQQLDAbshJzrsoQxJDAiBgNVBAMMG+yXkOyKpOy8gOydtCDtlIzrnpjri5so7KO8KTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMvxgBh37MP2f5CnALAY1z8/Vb+JR52Sjic2bXFI/B3NYUgDk6q707t0uHA4WslZMGQSq0unqx880o8NXpUyA7XzO//TGNV8yNYJ5rBK/YGm7FdCBLSfV6moLhkj0WmGlpYjAZcwX2wvEKEjpATBw+ClfaxOUdtbIdyxwqFV9NEQjc1R8G4g7aZBd6bT7Zfv6V9Vdjk+i56fdOAIw6PaYoIodsCWsuAX6szSgGkZmX8/m147BkSGLhovWnz6PYeR+RtHwxiTof26sE6ScKCV3qqId+otlO7jUWr8YMQ8NNmDUS+wudNCS7mPCSMo+3xsm6oqdQQIAKYvyus8tIIuI3ECAwEAAaOCAo8wggKLMIGPBgNVHSMEgYcwgYSAFLZ0qZuSPMdRsSKkT7y3PP4iM9d2oWikZjBkMQswCQYDVQQGEwJLUjENMAsGA1UECgwES0lTQTEuMCwGA1UECwwlS29yZWEgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkgQ2VudHJhbDEWMBQGA1UEAwwNS0lTQSBSb290Q0EgNIICEAQwHQYDVR0OBBYEFLx+WC76Agm3/Pty43ggfjyQjWZvMA4GA1UdDwEB/wQEAwIGwDCBgwYDVR0gAQH/BHkwdzB1BgoqgxqMmkQFBAEDMGcwLQYIKwYBBQUHAgEWIWh0dHA6Ly9nY2EuY3Jvc3NjZXJ0LmNvbS9jcHMuaHRtbDA2BggrBgEFBQcCAjAqHii8+AAgx3jJncEcx1gAIMcg1qiuMKwEx0AAIAAxsUQAIMeFssiy5AAuMHoGA1UdEQRzMHGgbwYJKoMajJpECgEBoGIwYAwb7JeQ7Iqk7LyA7J20IO2UjOuemOuLmyjso7wpMEEwPwYKKoMajJpECgEBATAxMAsGCWCGSAFlAwQCAaAiBCARdfi/q+spzK0OUo4ZsARo1VF+YRa7vuyF8SFbeVYstjB+BgNVHR8EdzB1MHOgcaBvhm1sZGFwOi8vZGlyLmNyb3NzY2VydC5jb206Mzg5L2NuPXMxZHA5cDQzOSxvdT1jcmxkcCxvdT1BY2NyZWRpdGVkQ0Esbz1Dcm9zc0NlcnQsYz1LUj9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0MEYGCCsGAQUFBwEBBDowODA2BggrBgEFBQcwAYYqaHR0cDovL29jc3AuY3Jvc3NjZXJ0LmNvbToxNDIwMy9PQ1NQU2VydmVyMA0GCSqGSIb3DQEBCwUAA4IBAQBtV7pMR/Ar4bphc2L+KDQ+8+LkYl5VRdGJHFWFSTU3txgzQnXMidmim0Syu0qX4ki1+t3nttIU8skGNeNtbFovsD92Lu1Ygg8nWRsimz6R8ICrx8vkqBcd5exRiU7L8wU7DQF7Vo1H0gAlKgFlEfmjI7Tt2TabALNiKzftvaMKvEH+/RT7OHEnC4sSPUMUMDpP+qodqnAE7T+W6FOmPv9rNkC+euN5BRpm8SJD5qAI9OUUtQSUneH+SUKeiktz04JSFxEILGq4WIwVVwvGh3bOgutHGzO7OEAlTa9mGhzBQw7r/+M4lUF9XDFOHMSLvpR74cDubLQS0gpMiiNhoBtfMYIBgjCCAX4CAQEwVzBPMQswCQYDVQQGEwJLUjESMBAGA1UECgwJQ3Jvc3NDZXJ0MRUwEwYDVQQLDAxBY2NyZWRpdGVkQ0ExFTATBgNVBAMMDENyb3NzQ2VydENBMgIEARQXTzANBglghkgBZQMEAgEFADANBgkqhkiG9w0BAQsFAASCAQBc1hlgptiBPSaYdhRSg/mZtoPwi5PU4wdV3QNA+b2qXtIgvPPbgPp2yj8IvF5QKGIM+hZ5keKg0dBGKzudqTfLJk1V1kAApzC++tHEClANEZrY5F8l9ZMecqLc17Et0mdtHRiiU/Ek+drheVOWbF1D8arElYtJ1xsybppkuTI+pnNxILFFLBm5nUeuqJ1E3/VmEYhV0GKUfNEZd/Z58NFpavp6q6Ljw3+svkoIt8sC+G2vUIdpw3dDI4FYow072K1BJ28BUB+1jAieu60v3237mD7o1ttDPOHPuHdIto5tkAaiFK9fk3PZwj5OeItPvShQjxj3moUTa3OklwFkNdTG
     * product : [{"name":"165금화","kind":"consumable","id":"0910067101","price":3300}]
     */

    private ResultBean result;

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String code;
        private String message;
        private int count;
        private String txid;
        private String receipt;
        /**
         * name : 165금화
         * kind : consumable
         * id : 0910067101
         * price : 3300
         */

        private List<ProductBean> product;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getReceipt() {
            return receipt;
        }

        public void setReceipt(String receipt) {
            this.receipt = receipt;
        }

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }

        public static class ProductBean {
            private String name;
            private String kind;
            private String id;
            private int price;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }
        }
    }
}
