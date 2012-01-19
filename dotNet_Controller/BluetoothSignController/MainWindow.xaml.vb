Imports System.IO.Ports
Imports System.Threading
Imports System.Text

Class MainWindow
    Shared repeat As String = "c"
    Shared serial As SerialPort = New SerialPort
    Dim dblSpeed As Double = 0D
    Dim msgspeed As String


    Public Sub New()

        ' This call is required by the designer.
        InitializeComponent()

        ' Add any initialization after the InitializeComponent() call.
        AddCom2Combo(cbPortsList)
        btnConnect.Content = "Connect"
        txtSpeed.Text = dblSpeed.ToString() + " ms"
    End Sub
    Public Sub Main()



    End Sub


    Private Sub BtnConnect_Click(ByVal sender As System.Object, ByVal e As System.Windows.RoutedEventArgs) Handles btnConnect.Click

        Try
            If btnConnect.Content = "Connect" Then
                'set up serial port
                With serial
                    '.PortName = cbPortsList.SelectedValue.ToString.Substring(0, cbPortsList.SelectedValue.ToString.Length - 1)
                    .PortName = cbPortsList.SelectedItem.ToString()
                    '.PortName = "COM26"
                    .BaudRate = 19200
                    .Parity = Parity.None
                    .Handshake = Handshake.None
                    .StopBits = StopBits.One
                    .DataBits = 8
                    .ReadTimeout = 200
                    .WriteTimeout = 50
                End With

                serial.Open()
                btnConnect.Content = "Dissconnect"
            Else
                serial.Close()
                btnConnect.Content = "Connect"
            End If

        Catch ex As Exception
            MessageBox.Show(ex.Message.ToString, "An Error has occured", MessageBoxButton.OK, MessageBoxImage.Error)
        End Try
    End Sub

    Private Sub BtnSend_Click(ByVal sender As System.Object, ByVal e As System.Windows.RoutedEventArgs) Handles btnSend.Click
        Dim msgColor As String = ""

        'check repeat box for value
        If chkRepeat.IsChecked Then
            repeat = "b"
        Else
            repeat = "c"
        End If

        If colorOrange.IsChecked Then
            msgColor = "f"
        End If

        If colorRed.IsChecked Then
            msgColor = "e"
        End If

        If colorGreen.IsChecked Then

            msgColor = "d"
        End If
        If Slider1.Value = 10 Then
            msgspeed = "q"
        ElseIf Slider1.Value = 20 Then
            msgspeed = "r"
        ElseIf Slider1.Value = 30 Then
            msgspeed = "s"
        ElseIf Slider1.Value = 40 Then
            msgspeed = "t"
        ElseIf Slider1.Value = 50 Then
            msgspeed = "u"
        ElseIf Slider1.Value = 60 Then
            msgspeed = "v"
        ElseIf Slider1.Value = 70 Then
            msgspeed = "w"
        ElseIf Slider1.Value = 80 Then
            msgspeed = "x"
        ElseIf Slider1.Value = 90 Then
            msgspeed = "y"
        ElseIf Slider1.Value = 100 Then
            msgspeed = "z"

        End If
        Dim DisplayString = repeat + "o" + msgColor + msgspeed + txtMessage.Text + "a"
        Try

            Dim hexstring As Byte() = Encoding.Default.GetBytes(DisplayString)
            For Each hexval As Byte In hexstring
                Dim _hexval As Byte() = New Byte() {hexval}
                ' need to convert byte 
                ' to byte[] to write
                serial.Write(_hexval, 0, 1)
                Thread.Sleep(1)
            Next

        Catch ex As Exception
            MessageBox.Show(ex.Message.ToString, "Failed to Send", MessageBoxButton.OK, MessageBoxImage.Error)
        End Try


    End Sub

    Private Sub Slider1_ValueChanged(ByVal sender As System.Object, ByVal e As System.Windows.RoutedPropertyChangedEventArgs(Of System.Double)) Handles Slider1.ValueChanged

        dblSpeed = Slider1.Value

        txtSpeed.Text = dblSpeed.ToString() + " ms"
    End Sub
End Class
