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
        <p>Hi ${reciever},</p>
        <p>Your friend ${friend_username} has registered you to ${app_name}</p>
        <p>You can use following credentials:</p>
        <p>Username: <b>${username}</b></p>
        <p>Password: <b>${password}</b></p>
        <p style="color: 'red'">We suggest you to change your password when you login!</p>
        <p>Your friend wait for you, login from <a href=${login_url}>here</a> and say him 'Hi'</p>
        <p>${app_name}</p>
      </td>
    </tr>
  </table>
</body>
</html>
