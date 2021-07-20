import UIKit
import SharedCode

class ResultsTableCell : UITableViewCell {
    @IBOutlet weak var departureTime: UILabel!
    @IBOutlet weak var arrivalTime: UILabel!
    @IBOutlet weak var departureExtraDay: UILabel!
    @IBOutlet weak var arrivalExtraDay: UILabel!
    @IBOutlet weak var departureStation: UILabel!
    @IBOutlet weak var arrivalStation: UILabel!
    @IBOutlet weak var status: UITextView!
    @IBOutlet weak var arrow: UIImageView!
}

class StationsTableCell : UITableViewCell {
    @IBOutlet weak var stationName: UILabel!
    @IBOutlet weak var stationCrs: UILabel!
    @IBOutlet weak var stationNlc: UILabel!
}

class SelectionReturn {
    var type: String?
    var stationSelected: Station
    
    init (type: String?, stationSelected: Station) {
        self.type = type
        self.stationSelected = stationSelected
    }
}

