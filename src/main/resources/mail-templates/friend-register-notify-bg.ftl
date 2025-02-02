<!doctype html>
<html lang="en">
<head>
  <style>
    p {
      margin-bottom: 1rem !important;
    }
    td {
      font-family: 'Open sans', Arial, sans-serif;
      font-size: 15px;
      line-height: 25px;
    }
  </style>
</head>
<body>
  <table>
    <tr>
      <td>
        <p>Здравейте ${reciever},</p>
        <p>Вашият приятел ${friend_username} ви регистрира за ${app_name}</p>
        <p>Можете да използвате следните данни:</p>
        <p>Потребителско име: <b>${username}</b></p>
        <p>Парола: <b>${password}</b></p>
        <p style="color: 'red'">Препоръчваме ви да смените паролата щом влезете!</p>
        <p>Вашият приятел ви очаква, влезте от <a href=${login_url}>тук</a> и му кажете 'Здравей'</p>
        <p>${app_name}</p>
      </td>
    </tr>
  </table>
</body>
</html>
