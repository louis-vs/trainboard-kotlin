import UIKit

class ResultsTableCell : UITableViewCell {
    @IBOutlet weak var departureTime: UILabel!
    @IBOutlet weak var arrivalTime: UILabel!
    @IBOutlet weak var departureStation: UILabel!
    @IBOutlet weak var arrivalStation: UILabel!
    @IBOutlet weak var status: UITextView!
    @IBOutlet weak var arrow: UIImageView!
    @IBOutlet weak var extraDay: UILabel!
}

class StationsTableCell : UITableViewCell {
    @IBOutlet weak var stationName: UILabel!
    @IBOutlet weak var stationCrs: UILabel!
    @IBOutlet weak var stationNlc: UILabel!
}
