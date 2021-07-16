import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet private var mainText: UILabel!
    @IBOutlet private var subHeader: UILabel!
    @IBOutlet private var picker: UIPickerView!
    @IBOutlet private var button: UIButton!
    @IBOutlet private var resultsTable: UITableView!
    
    private var pickerData: [Station] = [Station]()
    private var fromSelected: Station!
    private var toSelected: Station!
    private var journeyCollection: JourneyCollection!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        button.setTitle("Loading...", for: .disabled)
        
    }
    
    @IBAction func sendSelection() {
        presenter.runSearch(from: fromSelected, to: toSelected)
    }
}

extension ViewController: ApplicationContractView {
    func disableSearchButton() {
        button.isEnabled = false
    }
    
    func enableSearchButton() {
        button.isEnabled = true
    }
    
    func setTitle(title: String) {
        mainText.text = title
    }
    
    func displayErrorMessage(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default))
        self.present(alert, animated: true, completion: nil)
    }
    
    func displayJourneys(journeyCollection: JourneyCollection) {
        self.journeyCollection = journeyCollection
        resultsTable.dataSource = self
        resultsTable.reloadData()
    }
    
    func openUrl(url: String) {
        if let page = URL(string: url) {
            UIApplication.shared.open(page)
        }
    }
    
    func setStations(stations: [Station]) {
        pickerData = stations
        fromSelected = stations[0]
        toSelected = stations[1]
        picker.dataSource = self
        picker.delegate = self
        picker.selectRow(1, inComponent: 1, animated: false)
    }
}

extension ViewController: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
    }
}

extension ViewController: UIPickerViewDelegate {
    func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        let label = UILabel()
        label.text = pickerData[row].stationName
        label.textAlignment = .center
        label.font = UIFont(name: "System", size: 10)
        return label
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        switch component {
        case 0: fromSelected = pickerData[row]
        case 1: toSelected = pickerData[row]
        default: break
        }
    }
}

extension ViewController : UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "results_item", for: indexPath) as! ResultsTableCell
        let journey = journeyCollection.outboundJourneys[indexPath.item]
        
        cell.departureTime.text = journey.departureTimeFormatted
        cell.arrivalTime.text = journey.arrivalTimeFormatted
        cell.departureStation.text = journey.originStation.displayName
        cell.arrivalStation.text = journey.destinationStation.displayName
        cell.status.text = journey.status
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return journeyCollection.outboundJourneys.count
    }
}

class ResultsTableCell : UITableViewCell {
    @IBOutlet weak var departureTime: UILabel!
    @IBOutlet weak var arrivalTime: UILabel!
    @IBOutlet weak var departureStation: UILabel!
    @IBOutlet weak var arrivalStation: UILabel!
    @IBOutlet weak var status: UILabel!
}


