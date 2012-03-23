Public Class frmOptions
    Dim dblSpeed As Double = 0D
    Public Sub New()

        ' This call is required by the designer.
        InitializeComponent()

        ' Add any initialization after the InitializeComponent() call.
        txtSpeed.Text = dblSpeed.ToString() + " ms"
    End Sub
    Private Sub Slider1_ValueChanged(ByVal sender As System.Object, ByVal e As System.Windows.RoutedPropertyChangedEventArgs(Of System.Double)) Handles Slider1.ValueChanged

        dblSpeed = Slider1.Value

        txtSpeed.Text = dblSpeed.ToString() + " ms"
    End Sub

    Private Sub Button2_Click(ByVal sender As System.Object, ByVal e As System.Windows.RoutedEventArgs) Handles Button2.Click
        Me.Hide()

    End Sub
End Class
