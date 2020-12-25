package com.example.wugui.alipay.config;

public class AlipayConfig {

    /**
     * 应用ID
     */
    public static final String APP_ID = "2021000116679881";

    /**
     * 商户私钥
     */
    public static final String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCmudy6yqUNqo9A1WLr1Pzc58Q4+IO4qS4+MdV9Gq3BdMOn0UgX8WcIS6X4uZZQ1Y6GExifb40sCC+kPUs4YiW46OB4075dZJimZppcPX+rmi2o3zw64558tJT57taRByOg57TAf84PvwvzKAsvf+Y/OOZCljamQ1iEy3iQPmcbIBNqSlwFuC9jya26tzuUUKAK2vqbsYjJoQhZpQJrrdRxzSjH1craFpy3ZrZZd1tqm00FEUdT/0AATc+uPxN27MzqMmmRU7VA093JMXB3pIFaN5I7L/slcrCdHwDP93ZDQF22CfqkgxgIobetD3BgSxBOkK5nwvan3NFTadFnzNvBAgMBAAECggEAFQIigB78wUYWmvLIbjL+Uc+aeBtIbcJTo16gKg1si43tAX5reSjf9Lmjhj3MZQ8j0F0joyNpRH0bk7RtgANOAYjGkDMOL7J3UbBoDuto5PSegELw0fLQi6Iti3i+7LNbl/DaY8825eEK3m7TSf7xwlGnIgjPhg/CXalN3c8fKtmhBWglH2iAs0kooPEftMc2cmgAp8kgOyPlNevLV1mRgOFKHhxQ9cJxCK3RP0xrPxE4B4rWyjNmfHai9TejbcBBLGn47wlsDOcTvMBPkTaezeaOmmLRXLQ13IaJtTIjWgiZ50ILsUUSF+4mGC6KKnOjdQAAGbgrThtE+SajZi1jYQKBgQD1CxhqDE349C9fTwXynmf+rw8UoeXwJjPpv2GVLcyCmOXUoqhrPWtLImn2M3w7Zs68UaLSrFFUUlUANIAJ//9+RsDhx6P0GO8vhMWQmulNXa+zazg3W0uLABpf7DaHWuF3VYfAErM4AplIfdhNKGyG9K9Sbunj+sdqTJjPu9allQKBgQCuLk10/sIwHlMSB3IGQEfjF0WiZtxl2gt1ORPVThiZAosaYR5GMH82NHz4SUUDQzYOWi+Tr1yOAEYUvBF6QyqY1RICBYGHy5oDJri+xMsTHCu41r5sEuSKdx+XaW50oy37xYJSxV9tCyvl19ZIM6jI8ZzAq2amegoVuyA54YF6fQKBgHfr0ynrFam1jw18tfNzlH9umtnFFau+dn95mr8QJ9ruCuRSKkw5Ra4CjIrhzXWgraI57Y5eqHq0xEaSaur57saUx8Z3FoTaQwwlVS6csXqcU+VhnqRwuHqxNTcmedUqI9dr80CLpkvN8cgpVlcEZoBxdO/JugtJaphaHhrj2Xs1AoGBAJzunN0HV6CRAdlhmetl6R8C7btnn7WooeAo3+S0a1cYwomu7N7ifQsyeVIeea551O9TdEBEpEhQmNO/qqOUdoGin4nwugbGe2nRbmx61FjQbdgoqwKmdwEGWRFLbbhq+Vg5WLUInXSatx9ruuXsnWsGvWueQmtF02ZG5b8gnU7lAoGBAIK+4oRMPIKSDBdD+JYq6FvBp0pUpQ1sVBSVrlXubtIrMuJIqkTgGUbVXsLH7pgD90j+OPQTzSd14Y3bZt3HbQGntNSmfSLGLO1t+H33ZXwmDjdSDQnRHmJ0Pmfs/VyM+bv4rVmLsBVgS8Zqi9uBgAyBxSHPUzxQ5e2nzO+40HXF";

    /**
     * 对应APPID下的支付宝公钥
     */
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqF2hCFlHfHxXbZyEP1AoHXmsHRx9tZzfrZakm6acQMQ0GQ4GofXEcZvFgzXSr6se0RRScmz5JrFINGnXRYoiMxm+VUb4bMPVVye+UDVk6B8f6OTO+ixuQC4NlNn2a2BNDTBHo8yIEXPktFO+WVVtGkFYQ3/yL8XW7HWmBTqaYpwP7SwQrdTjTVYWw3sSW3C6TRfg5o0V7p9HDOg3XM47YyheLniEcOVawslUXpQpoYOXiwnYCfZrLmKXIwe+LjYAj7y6UAoevv43Huub/nSM/Ywzi3zXdtkjmloqRZ/rvdumJ1s++n+CQO17bSwRtCD2/WV9mygIUZi1xFVx0+4ByQIDAQAB";

    /**
     * 异步通知URL
     */
    public static final String NOTIFY_URL = "http://www.baidu.com";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public static final String RETURN_URL = "http://www.baidu.com";

    /**
     * 签名方式
     */
    public static final String SIGN_TYPE = "RSA2";

    /**
     * 字符编码格式
     */
    public static final String CHARSET = "utf-8";

    /**
     * 支付宝网关
     * https://openapi.alipaydev.com/gateway.do (沙箱环境)
     * https://openapi.alipay.com/gateway.do (正式环境)
     */
    public static final String GATEWAYURL = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 格式
     */
    public static final String FORMAT = "json";
}
