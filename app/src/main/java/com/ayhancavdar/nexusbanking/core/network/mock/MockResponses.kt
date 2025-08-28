/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.network.mock

object MockResponses {
    const val LOGIN_SUCCESS = """
    {
      "success": true,
      "errorId": 0,
      "errorMsg": null,
      "title": null,
      "customerName": "Ayhan Cavdar",
      "changePassword": false,
      "newUser": false,
      "customerId": "12000034",
      "smsOtpNumbers": [
        {
          "number": "5555158168",
          "starredNumber": "555 - *****68",
          "id": "1"
        },
        {
          "number": "5556448817",
          "starredNumber": "555 - *****17",
          "id": "2"
        }
      ],
      "lastLoginInfo": {
        "date": "2014-04-22T17:36:48.000+03:00",
        "ip": null,
        "channel": "1"
      }
    }
    """

    const val LOGIN_ERROR = """
    {
      "success": false,
      "errorId": 1,
      "errorMsg": "Giris basarisiz",
      "title": "INFO"
    }
    """
}
